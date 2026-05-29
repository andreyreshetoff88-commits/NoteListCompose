package ru.reshetoff.register_data.utils

import ru.reshetoff.register_data.model.CountryDto
import ru.reshetoff.register_domain.model.Country

fun CountryDto.toDomain() = Country(
    id = this.id,
    countryCode = this.countryCode,
    phoneCode = this.phoneCode,
    phoneMask = this.phoneMask,
    flagUrl = this.flagUrl
)