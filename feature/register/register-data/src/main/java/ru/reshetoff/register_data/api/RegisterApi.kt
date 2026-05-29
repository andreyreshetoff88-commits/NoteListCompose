package ru.reshetoff.register_data.api

import retrofit2.Response
import retrofit2.http.GET
import ru.reshetoff.register_data.model.CountryDto

interface RegisterApi {
    @GET("utils/countries")
    suspend fun getCountryList(): Response<List<CountryDto>>
}