package helpers

import tech.sergeyev.education.api.v1.models.PartError

fun Throwable.asPartError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = PartError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)
