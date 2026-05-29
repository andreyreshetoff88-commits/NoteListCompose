package ru.reshetoff.register_data.utils

import ru.reshetoff.register_data.model.CountryDto
import ru.reshetoff.register_data.model.RegisterRequestDto
import ru.reshetoff.register_domain.model.Country
import ru.reshetoff.register_domain.model.RegisterRequest

fun CountryDto.toDomain() = Country(
    id = this.id,
    countryCode = this.countryCode,
    phoneCode = this.phoneCode,
    phoneMask = this.phoneMask,
    flagUrl = this.flagUrl
)

fun RegisterRequest.toDto() = RegisterRequestDto(
    displayName = this.displayName,
    phoneNumber = this.phoneNumber,
    email = this.email,
    password = this.password
)