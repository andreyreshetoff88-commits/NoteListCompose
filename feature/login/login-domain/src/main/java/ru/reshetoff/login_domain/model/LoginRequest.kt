package ru.reshetoff.login_domain.model

data class LoginRequest(
    val email: String,
    val password: String
)
