package tech.sergeyev.education.api.v1.models

data class PartError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
)
