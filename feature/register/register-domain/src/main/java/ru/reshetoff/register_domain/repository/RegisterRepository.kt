package ru.reshetoff.register_domain.repository

import ru.reshetoff.register_domain.model.Country

interface RegisterRepository {
    suspend fun getCountryList(): Result<List<Country>>
}