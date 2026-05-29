package ru.reshetoff.login_presentation.utils

import ru.reshetoff.login_domain.utils.LoginResource

sealed class LoginState {
    class Empty() : LoginState()
    class Loading() : LoginState()
    class Success() : LoginState()
    class Error(val message: String) : LoginState()
}