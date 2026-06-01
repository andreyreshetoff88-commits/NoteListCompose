package ru.reshetoff.app_data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import okio.IOException
import retrofit2.HttpException
import ru.reshetoff.app_data.api.AppApi
import ru.reshetoff.app_data.utils.toEntity
import ru.reshetoff.app_domain.repository.AppRepository
import ru.reshetoff.database.SharedPref
import ru.reshetoff.database.dao.ProfileDao
import ru.reshetoff.network.NetworkUtils
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val sharedPref: SharedPref,
    private val profileDao: ProfileDao,
    private val appApi: AppApi,
    private val networkUtils: NetworkUtils
) : AppRepository {
    override fun hasAccessToken(): Flow<Boolean> {
        return sharedPref.observeAccessToken().map { !it.isNullOrBlank() }.distinctUntilChanged()
    }

    override suspend fun sync(): Result<Boolean> {
        return try {
            val response = appApi.getMe()
            if (response.isSuccessful) {
                val profileResponse =
                    response.body() ?: return Result.failure(Exception("Пустой ответ от сервера"))

                profileDao.insertProfile(profileEntity = profileResponse.toEntity())

                Result.success(profileResponse.isVerified)
            } else {
                val errorMessage = networkUtils.parseErrorResponse(response)
                Result.failure(Exception(errorMessage))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Проверьте подключение к интернету"))
        } catch (e: HttpException) {
            Result.failure(Exception("Серверная ошибка. Попробуйте позже"))
        } catch (e: Exception) {
            Result.failure(exception = e)
        }
    }
}