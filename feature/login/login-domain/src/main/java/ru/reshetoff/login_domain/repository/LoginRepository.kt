package ru.reshetoff.login_domain.repository

import ru.reshetoff.login_domain.model.LoginRequest

interface LoginRepository {
    suspend fun login(loginRequest: LoginRequest): Result<Unit>
}