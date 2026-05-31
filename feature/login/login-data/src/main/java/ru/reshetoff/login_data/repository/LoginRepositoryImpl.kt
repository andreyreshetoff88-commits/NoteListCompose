package ru.reshetoff.login_data.repository

import okio.IOException
import retrofit2.HttpException
import ru.reshetoff.database.SharedPref
import ru.reshetoff.login_data.api.LoginApi
import ru.reshetoff.login_data.model.LoginResponse
import ru.reshetoff.login_data.utils.toDto
import ru.reshetoff.login_domain.model.LoginRequest
import ru.reshetoff.login_domain.repository.LoginRepository
import ru.reshetoff.network.NetworkUtils
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginApi: LoginApi,
    private val sharedPref: SharedPref,
    private val networkUtils: NetworkUtils
) : LoginRepository {
    override suspend fun login(loginRequest: LoginRequest): Result<Unit> {
        return try {
            val response = loginApi.login(loginRequest.toDto())
            if (response.isSuccessful) {
                val loginResponse =
                    response.body() ?: return Result.failure(Exception("Пустой ответ от сервера"))

                saveTokens(loginResponse)

                Result.success(Unit)
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

    private fun saveTokens(loginResponse: LoginResponse) {
        sharedPref.accessToken = loginResponse.accessToken
        sharedPref.refreshToken = loginResponse.refreshToken
    }
}