package ru.reshetoff.network.model

data class RefreshResponse (
    val accessToken: String,
    val refreshToken: String
)