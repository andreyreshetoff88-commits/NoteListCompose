package ru.reshetoff.register_data.repository

import android.util.Log
import okio.IOException
import retrofit2.HttpException
import ru.reshetoff.register_data.api.RegisterApi
import ru.reshetoff.register_data.utils.toDomain
import ru.reshetoff.register_domain.model.Country
import ru.reshetoff.register_domain.repository.RegisterRepository
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val registerApi: RegisterApi
) : RegisterRepository {
    override suspend fun getCountryList(): Result<List<Country>> {
        return try {
            val response = registerApi.getCountryList()
            if (response.isSuccessful) {
                val countries = response.body()
                if (countries.isNullOrEmpty()) {
                    Log.d("ololo", "login repository -> login: Пустой ответ от сервера")
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
}