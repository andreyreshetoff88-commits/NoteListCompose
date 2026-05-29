package ru.reshetoff.register_data.model

data class RegisterRequestDto(
    val displayName: String,
    val phoneNumber: String,
    val email: String,
    val password: String
)
