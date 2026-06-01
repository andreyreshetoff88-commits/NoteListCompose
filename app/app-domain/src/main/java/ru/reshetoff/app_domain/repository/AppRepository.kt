package ru.reshetoff.app_domain.repository

import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun hasAccessToken(): Flow<Boolean>
    suspend fun sync(): Result<Boolean>
}