package ru.reshetoff.register_data.repository

import android.util.Log
import com.google.gson.Gson
import okio.IOException
import retrofit2.HttpException
import ru.reshetoff.common.model.errorresponse.ErrorResponse
import ru.reshetoff.database.SharedPref
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
    private val gson: Gson
) : RegisterRepository {
    override suspend fun getCountryList(): Result<List<Country>> {
        return try {
            val response = registerApi.getCountryList()
            if (response.isSuccessful) {
                val countries = response.body()
                if (countries.isNullOrEmpty()) {
                    Log.d("ololo", "register repository -> getCountryList: Пустой ответ от сервера")
                    return Result.failure(Exception("Пустой ответ от сервера"))
                }

                Log.d("ololo", "getCountryList: $countries")
                Result.success(countries.map { it.toDomain() })
            } else {
                Result.failure(Exception(response.code().toString()))
            }
        } catch (e: IOException) {
            Log.e("ololo", "Network error: ${e.message}")
            Result.failure(Exception("Проверьте подключение к интернету"))
        } catch (e: HttpException) {
            Log.e("ololo", "HTTP error: ${e.code()} - ${e.message}")
            Result.failure(Exception("Серверная ошибка. Попробуйте позже"))
        } catch (e: Exception) {
            Result.failure(exception = e)
        }
    }

    override suspend fun register(registerRequest: RegisterRequest): Result<Unit> {
        return try {
            val response = registerApi.register(registerRequest.toDto())
            if (response.isSuccessful) {
                val registerResponse = response.body()

                if (registerResponse == null) {
                    Log.d("ololo", "register repository -> register: Пустой ответ от сервера")
                    return Result.failure(Exception("Пустой ответ от сервера"))
                }

                saveTokens(registerResponse)

                Log.d(
                    "ololo",
                    "register repository -> register: access token = ${sharedPref.accessToken}\n refresh token = ${sharedPref.refreshToken}"
                )
                Result.success(Unit)
            } else {
                val errorMessage = parseErrorResponse(response)
                Log.e("ololo", "Login failed: ${response.code()} - $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: IOException) {
            Log.e("ololo", "Network error: ${e.message}")
            Result.failure(Exception("Проверьте подключение к интернету"))
        } catch (e: HttpException) {
            Log.e("ololo", "HTTP error: ${e.code()} - ${e.message}")
            Result.failure(Exception("Серверная ошибка. Попробуйте позже"))
        } catch (e: Exception) {
            Result.failure(exception = e)
        }
    }

    private fun saveTokens(registerResponse: RegisterResponse) {
        sharedPref.accessToken = registerResponse.accessToken
        sharedPref.refreshToken = registerResponse.refreshToken
    }

    private fun parseErrorResponse(response: retrofit2.Response<*>): String {
        return try {
            val errorBody = response.errorBody()?.string()
            if (!errorBody.isNullOrBlank()) {
                val error = gson.fromJson(errorBody, ErrorResponse::class.java)

                val message = error.message
                if (!message.isNullOrBlank()) {
                    return message
                }

                val details = error.details
                if (!details.isNullOrEmpty()) {
                    return details.joinToString(", ") {
                        "${it.field ?: "Поле"}: ${it.message ?: "Ошибка"}"
                    }
                }

                val code = error.code
                if (!code.isNullOrBlank()) {
                    return "Ошибка: $code"
                }

                return "Ошибка ${response.code()}"
            } else {
                "Ошибка ${response.code()}"
            }
        } catch (e: Exception) {
            "Ошибка ${response.code()}"
        }
    }
}