package ru.reshetoff.app_domain.usecase

import kotlinx.coroutines.flow.flow
import ru.reshetoff.app_domain.repository.AppRepository
import ru.reshetoff.app_domain.utils.Resource
import javax.inject.Inject

class SyncUseCase @Inject constructor(private val appRepository: AppRepository) {
    fun execute() = flow {
        emit(Resource.Loading())
        appRepository.sync().fold(
            onSuccess = { data ->
                emit(Resource.Success(data = data))
            },
            onFailure = { exception ->
                emit(Resource.Error(message = exception.localizedMessage))
            }
        )
    }
}