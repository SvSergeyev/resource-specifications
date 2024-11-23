package helpers

import AppContext
import models.State
import models.PartError
import tech.sergeyev.education.logging.common.LogLevel

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

inline fun AppContext.addError(error: PartError) = errors.add(error)
inline fun AppContext.addErrors(error: Collection<PartError>) = errors.addAll(error)

inline fun AppContext.fail(error: PartError) {
    addError(error)
    state = State.FAILING
}

inline fun AppContext.fail(errors: Collection<PartError>) {
    addErrors(errors)
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
inline fun errorSystem(
    violationCode: String,
    level: LogLevel = LogLevel.ERROR,
    e: Throwable,
) = PartError(
    code = "system-$violationCode",
    group = "system",
    message = "System error occurred. Our stuff has been informed, please retry later",
    exception = e,
)