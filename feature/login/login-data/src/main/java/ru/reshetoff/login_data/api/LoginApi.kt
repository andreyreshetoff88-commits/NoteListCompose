package ru.reshetoff.login_data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.reshetoff.login_data.model.LoginRequestDto
import ru.reshetoff.login_data.model.LoginResponse

interface LoginApi {
    @POST("auth/login")
    suspend fun login(@Body loginRequestDto: LoginRequestDto): Response<LoginResponse>
}