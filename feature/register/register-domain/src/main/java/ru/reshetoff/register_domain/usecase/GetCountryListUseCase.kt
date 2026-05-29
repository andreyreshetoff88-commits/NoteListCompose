package ru.reshetoff.register_domain.usecase

import kotlinx.coroutines.flow.flow
import ru.reshetoff.register_domain.repository.RegisterRepository
import ru.reshetoff.utils.Resource
import javax.inject.Inject

class GetCountryListUseCase @Inject constructor(private val registerRepository: RegisterRepository) {
    fun execute() = flow {
        emit(Resource.Loading())
        registerRepository.getCountryList().fold(
            onSuccess = { data ->
                emit(Resource.Success(data = data))
            },
            onFailure = { exception ->
                emit(Resource.Error(message = exception.localizedMessage))
            }
        )
    }
}