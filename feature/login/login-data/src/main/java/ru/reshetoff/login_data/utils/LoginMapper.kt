package ru.reshetoff.login_data.utils

import ru.reshetoff.login_data.model.LoginRequestDto
import ru.reshetoff.login_domain.model.LoginRequest

fun LoginRequest.toDto() = LoginRequestDto(
    email = this.email,
    password = this.password
)