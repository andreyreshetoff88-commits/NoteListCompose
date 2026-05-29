package ru.reshetoff.common.model.errorresponse

data class ErrorResponse(
    val code: String?,
    val level: String?,
    val message: String?,
    val details: List<ErrorDetail>? = null
)
