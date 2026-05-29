package ru.reshetoff.register_domain.repository

import ru.reshetoff.register_domain.model.Country
import ru.reshetoff.register_domain.model.RegisterRequest

interface RegisterRepository {
    suspend fun getCountryList(): Result<List<Country>>
    suspend fun register(registerRequest: RegisterRequest): Result<Unit>
}