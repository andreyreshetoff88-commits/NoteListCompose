package ru.reshetoff.network.interceptor

import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import ru.reshetoff.database.SharedPref
import ru.reshetoff.network.api.RefreshApi
import ru.reshetoff.network.model.RefreshRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val sharedPref: SharedPref,
    private val refreshApi: RefreshApi
) : Interceptor {
    @Volatile
    private var isRefreshing = false

    @Volatile
    private var newAccessToken: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val accessToken = sharedPref.accessToken

        if (!accessToken.isNullOrEmpty()) {
            request = request.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        }

        val response = chain.proceed(request)

        if (response.code == 401) {
            response.close()
            return refreshTokenAndRetry(chain, request)
        }

        return response
    }

    private fun refreshTokenAndRetry(chain: Interceptor.Chain, originalRequest: Request): Response {
        synchronized(this) {
            if (!isRefreshing) {
                isRefreshing = true

                try {
                    val refreshToken = sharedPref.refreshToken
                    if (refreshToken.isNullOrEmpty()) {
                        return Response.Builder()
                            .code(401)
                            .build()
                    }

                    val response = runBlocking {
                        Log.d("ololo", "refreshTokenAndRetry: refresh token")
                        refreshApi.refreshToken(RefreshRequest(refreshToken))
                    }
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            newAccessToken = body.accessToken
                            sharedPref.accessToken = newAccessToken
                            sharedPref.refreshToken = body.refreshToken
                        } else {
                            sharedPref.accessToken = null
                            sharedPref.refreshToken = null
                            return Response.Builder()
                                .request(originalRequest)
                                .protocol(okhttp3.Protocol.HTTP_1_1)
                                .code(401)
                                .message("Unauthorized")
                                .body("".toResponseBody(null))
                                .build()
                        }
                    }
                } finally {
                    isRefreshing = false
                }
            } else {
                var retryCount = 0
                while (newAccessToken == null && retryCount < 10) {
                    Thread.sleep(200)
                    retryCount++
                }
            }
        }

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer ${newAccessToken ?: sharedPref.accessToken}")
            .build()

        return chain.proceed(newRequest)
    }
}