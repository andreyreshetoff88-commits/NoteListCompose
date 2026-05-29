package ru.reshetoff.common.model.errorresponse

data class ErrorDetail(
    val field: String?,
    val message: String?
)