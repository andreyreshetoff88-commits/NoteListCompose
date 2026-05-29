package ru.reshetoff.register_domain.usecase

import kotlinx.coroutines.flow.flow
import ru.reshetoff.register_domain.model.RegisterRequest
import ru.reshetoff.register_domain.repository.RegisterRepository
import ru.reshetoff.utils.Resource
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val registerRepository: RegisterRepository) {
    fun execute(registerRequest: RegisterRequest) = flow {
        emit(Resource.Loading())
        registerRepository.register(registerRequest = registerRequest).fold(
            onSuccess = {
                emit(Resource.Success(Unit))
            },
            onFailure = { exception ->
                emit(Resource.Error(message = exception.localizedMessage))
            }
        )
    }
}