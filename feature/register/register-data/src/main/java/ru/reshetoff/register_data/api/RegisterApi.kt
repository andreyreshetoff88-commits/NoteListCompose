package ru.reshetoff.register_data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.reshetoff.register_data.model.CountryDto
import ru.reshetoff.register_data.model.RegisterRequestDto
import ru.reshetoff.register_data.model.RegisterResponse

interface RegisterApi {
    @GET("utils/countries")
    suspend fun getCountryList(): Response<List<CountryDto>>

    @POST("auth/register")
    suspend fun register(@Body registerRequestDto: RegisterRequestDto): Response<RegisterResponse>

    @POST("auth/send-verification")
    suspend fun sendVerification(@Query("email") email: String): Response<String>
}