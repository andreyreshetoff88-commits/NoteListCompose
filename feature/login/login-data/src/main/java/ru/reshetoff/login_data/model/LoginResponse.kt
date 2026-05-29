package ru.reshetoff.login_data.model

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
)