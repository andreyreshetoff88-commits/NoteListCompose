package ru.reshetoff.register_data.model

import java.util.UUID

data class CountryDto(
    val id: UUID,
    val countryCode: String,
    val phoneCode: String,
    val phoneMask: String,
    val flagUrl: String
)
