package ru.reshetoff.register_domain.model

import java.util.UUID

data class Country(
    val id: UUID,
    val countryCode: String,
    val phoneCode: String,
    val phoneMask: String,
    val flagUrl: String
)
