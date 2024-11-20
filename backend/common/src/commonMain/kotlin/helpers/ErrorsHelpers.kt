package helpers

import AppContext
import models.State
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

inline fun AppContext.addError(vararg error: PartError) = errors.addAll(error)

inline fun AppContext.fail(error: PartError) {
    addError(error)
    state = State.FAILING
}

inline fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
) = PartError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
)
