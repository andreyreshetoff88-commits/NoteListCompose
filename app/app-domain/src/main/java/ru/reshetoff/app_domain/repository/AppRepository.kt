package ru.reshetoff.app_domain.repository

interface AppRepository {
    fun hasAccessToken(): Boolean
}