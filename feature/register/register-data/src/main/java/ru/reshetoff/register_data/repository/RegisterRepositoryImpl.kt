package ru.reshetoff.register_data.repository

import okio.IOException
import retrofit2.HttpException
import ru.reshetoff.database.SharedPref
import ru.reshetoff.network.NetworkUtils
import ru.reshetoff.register_data.api.RegisterApi
import ru.reshetoff.register_data.model.RegisterResponse
import ru.reshetoff.register_data.utils.toDomain
import ru.reshetoff.register_data.utils.toDto
import ru.reshetoff.register_domain.model.Country
import ru.reshetoff.register_domain.model.RegisterRequest
import ru.reshetoff.register_domain.repository.RegisterRepository
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val registerApi: RegisterApi,
    private val sharedPref: SharedPref,
    private val networkUtils: NetworkUtils
) : RegisterRepository {
    override suspend fun getCountryList(): Result<List<Country>> {
        return try {
            val response = registerApi.getCountryList()
            if (response.isSuccessful) {
                val countries = response.body()
                if (countries.isNullOrEmpty()) {
                    return Result.failure(Exception("Пустой ответ от сервера"))
                }

                Result.success(countries.map { it.toDomain() })
            } else {
                Result.failure(Exception(response.code().toString()))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Проверьте подключение к интернету"))
        } catch (e: HttpException) {
            Result.failure(Exception("Серверная ошибка. Попробуйте позже"))
        } catch (e: Exception) {
            Result.failure(exception = e)
        }
    }

    override suspend fun register(registerRequest: RegisterRequest): Result<Unit> {
        return try {
            val sendVerificationResponse = registerApi.sendVerification(registerRequest.email)
            if (!sendVerificationResponse.isSuccessful) {
                val errorMessage = networkUtils.parseErrorResponse(sendVerificationResponse)
                return Result.failure(Exception(errorMessage))
            }

            val response = registerApi.register(registerRequest.toDto())
            if (response.isSuccessful) {
                val registerResponse =
                    response.body() ?: return Result.failure(Exception("Пустой ответ от сервера"))

                saveTokens(registerResponse)

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

    private fun saveTokens(registerResponse: RegisterResponse) {
        sharedPref.accessToken = registerResponse.accessToken
        sharedPref.refreshToken = registerResponse.refreshToken
    }
}