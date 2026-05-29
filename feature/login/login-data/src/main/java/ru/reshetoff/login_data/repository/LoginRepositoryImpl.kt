package ru.reshetoff.login_data.repository

import android.util.Log
import com.google.gson.Gson
import okio.IOException
import retrofit2.HttpException
import ru.reshetoff.common.model.errorresponse.ErrorResponse
import ru.reshetoff.database.SharedPref
import ru.reshetoff.login_data.api.LoginApi
import ru.reshetoff.login_data.model.LoginResponse
import ru.reshetoff.login_data.utils.toDto
import ru.reshetoff.login_domain.model.LoginRequest
import ru.reshetoff.login_domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginApi: LoginApi,
    private val sharedPref: SharedPref,
    private val gson: Gson
) : LoginRepository {
    override suspend fun login(loginRequest: LoginRequest): Result<Unit> {
        return try {
            val response = loginApi.login(loginRequest.toDto())
            if (response.isSuccessful) {
                val loginResponse = response.body()

                if (loginResponse == null) {
                    Log.d("ololo", "login repository -> login: Пустой ответ от сервера")
                    return Result.failure(Exception("Пустой ответ от сервера"))
                }

                saveTokens(loginResponse)

                Log.d(
                    "ololo",
                    "login repository -> login: access token = ${sharedPref.accessToken}\n refresh token = ${sharedPref.refreshToken}"
                )
                Result.success(Unit)
            } else {
                val errorMessage = parseErrorResponse(response)
                Log.e("ololo", "Login failed: ${response.code()} - $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: IOException) {
            Log.e("ololo", "Network error: ${e.message}")
            Result.failure(Exception("Проверьте подключение к интернету"))
        } catch (e: HttpException) {
            Log.e("ololo", "HTTP error: ${e.code()} - ${e.message}")
            Result.failure(Exception("Серверная ошибка. Попробуйте позже"))
        } catch (e: Exception) {
            Result.failure(exception = e)
        }
    }

    private fun saveTokens(loginResponse: LoginResponse) {
        sharedPref.accessToken = loginResponse.accessToken
        sharedPref.refreshToken = loginResponse.refreshToken
    }

    private fun parseErrorResponse(response: retrofit2.Response<*>): String {
        return try {
            val errorBody = response.errorBody()?.string()
            if (!errorBody.isNullOrBlank()) {
                val error = gson.fromJson(errorBody, ErrorResponse::class.java)

                val message = error.message
                if (!message.isNullOrBlank()) {
                    return message
                }

                val details = error.details
                if (!details.isNullOrEmpty()) {
                    return details.joinToString(", ") {
                        "${it.field ?: "Поле"}: ${it.message ?: "Ошибка"}"
                    }
                }

                val code = error.code
                if (!code.isNullOrBlank()) {
                    return "Ошибка: $code"
                }

                return "Ошибка ${response.code()}"
            } else {
                "Ошибка ${response.code()}"
            }
        } catch (e: Exception) {
            "Ошибка ${response.code()}"
        }
    }
}