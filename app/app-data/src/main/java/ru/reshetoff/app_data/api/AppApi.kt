package ru.reshetoff.app_data.api

import retrofit2.Response
import retrofit2.http.GET
import ru.reshetoff.app_data.model.ProfileResponse

interface AppApi {
    @GET("auth/me")
    suspend fun getMe(): Response<ProfileResponse>
}