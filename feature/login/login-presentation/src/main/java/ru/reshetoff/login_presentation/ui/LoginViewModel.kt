package ru.reshetoff.login_presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.reshetoff.login_domain.model.LoginRequest
import ru.reshetoff.login_domain.usecase.LoginUseCase
import ru.reshetoff.login_domain.utils.LoginResource
import ru.reshetoff.login_presentation.utils.LoginState
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {
    private var _loginState = MutableStateFlow<LoginState>(LoginState.Empty())
    val loginState: StateFlow<LoginState> get() = _loginState

    fun login(email: String, password: String) {
        loginUseCase.execute(LoginRequest(email = email, password = password)).onEach { resource ->
            when (resource) {
                is LoginResource.Loading -> _loginState.value = LoginState.Loading()
                is LoginResource.Success -> _loginState.value = LoginState.Success()
                is LoginResource.Error -> _loginState.value =
                    LoginState.Error(message = resource.message)
            }
        }.launchIn(viewModelScope)
    }
}