package ru.reshetoff.register_domain.model

data class RegisterRequest(
    val displayName: String,
    val phoneNumber: String,
    val email: String,
    val password: String
)
