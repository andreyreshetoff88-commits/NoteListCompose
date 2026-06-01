package ru.reshetoff.app_data.utils

import ru.reshetoff.app_data.model.ProfileResponse
import ru.reshetoff.database.entity.ProfileEntity

fun ProfileResponse.toEntity() = ProfileEntity(
    id = this.id,
    displayName = this.displayName,
    phoneNumber = this.phoneNumber,
    email = this.email,
    isVerified = this.isVerified
)