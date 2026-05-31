package ru.reshetoff.network

import com.google.gson.Gson
import ru.reshetoff.network.model.errorresponse.ErrorResponse
import javax.inject.Inject

class NetworkUtils @Inject constructor(
    private val gson: Gson
) {
    fun parseErrorResponse(response: retrofit2.Response<*>): String {
        return try {
            val errorBody = response.errorBody()?.string()
            if (!errorBody.isNullOrBlank()) {
                val error = gson.fromJson(errorBody, ErrorResponse::class.java)

                when (error.code) {
                    "VALIDATION_FAILED" -> {
                        val details = error.details?.joinToString("\n") {
                            it.message ?: "Некорректное поле"
                        }
                        return details ?: "Данные заполнены неверно"
                    }

                    "USER_NOT_FOUND" -> return "Пользователь с таким email не найден"
                    "UNAUTHORIZED" -> return "Неверный email или пароль"
                    "ACCOUNT_NOT_VERIFIED" -> return "Аккаунт не подтверждён. Проверьте почту."
                    "EMAIL_ALREADY_EXISTS" -> return "Этот email уже используется"
                    "INVALID_VERIFICATION_TOKEN" -> return "Ссылка для подтверждения недействительна"
                    "INTERNAL_SERVER_ERROR" -> return "Техническая ошибка. Попробуйте позже."
                }

                val message = error.message
                if (!message.isNullOrBlank()) {
                    return message
                }

                "Ошибка ${response.code()}"
            } else {
                "Ошибка ${response.code()}"
            }
        } catch (e: Exception) {
            "Ошибка ${response.code()}"
        }
    }
}