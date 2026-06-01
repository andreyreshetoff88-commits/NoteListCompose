package ru.reshetoff.app_data.model

import java.util.UUID

data class ProfileResponse(
    val id: UUID,
    val displayName: String,
    val phoneNumber: String,
    val email: String,
    val isVerified: Boolean
)