package ru.reshetoff.network.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.reshetoff.network.model.RefreshRequest
import ru.reshetoff.network.model.RefreshResponse

interface RefreshApi {
    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshRequest
    ): Response<RefreshResponse>
}