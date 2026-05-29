package ru.reshetoff.login_domain.utils

sealed class LoginResource {
    class Loading() : LoginResource()
    class Success() : LoginResource()
    class Error(val message: String) : LoginResource()
}