package ru.reshetoff.app_domain.usecase

import ru.reshetoff.app_domain.repository.AppRepository
import javax.inject.Inject

class HasAccessTokenUseCase @Inject constructor(private val appRepository: AppRepository) {
    fun execute() = appRepository.hasAccessToken()
}