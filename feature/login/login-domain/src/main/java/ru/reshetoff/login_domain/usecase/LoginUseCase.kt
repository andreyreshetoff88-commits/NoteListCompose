package ru.reshetoff.login_domain.usecase

import kotlinx.coroutines.flow.flow
import ru.reshetoff.login_domain.model.LoginRequest
import ru.reshetoff.login_domain.repository.LoginRepository
import ru.reshetoff.login_domain.utils.LoginResource
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    fun execute(loginRequest: LoginRequest) = flow {
        emit(LoginResource.Loading())

        loginRepository.login(loginRequest = loginRequest).fold(
            onSuccess = {
                emit(LoginResource.Success())
            },
            onFailure = { exception ->
                emit(LoginResource.Error(message = exception.localizedMessage))
            }
        )
    }
}