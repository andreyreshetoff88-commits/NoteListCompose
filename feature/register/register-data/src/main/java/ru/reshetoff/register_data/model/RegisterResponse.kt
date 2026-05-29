package ru.reshetoff.register_data.model

data class RegisterResponse(
    val accessToken: String,
    val refreshToken: String
)