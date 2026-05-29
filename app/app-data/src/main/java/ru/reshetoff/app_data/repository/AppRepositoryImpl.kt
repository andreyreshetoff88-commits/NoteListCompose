package ru.reshetoff.app_data.repository

import ru.reshetoff.app_domain.repository.AppRepository
import ru.reshetoff.database.SharedPref
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val sharedPref: SharedPref
) : AppRepository {
    override fun hasAccessToken(): Boolean {
        return !sharedPref.accessToken.isNullOrBlank()
    }
}