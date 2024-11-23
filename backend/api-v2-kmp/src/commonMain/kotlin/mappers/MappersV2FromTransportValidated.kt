package mappers

import AppContext
import models.Command
import models.State
import stubs.Stubs
import tech.sergeyev.education.api.v1.models.PartError
import tech.sergeyev.education.api.v2.models.PartCreateRequest

// Демонстрация форматной валидации в мапере
private sealed interface Result<T,E>
private data class Ok<T,E>(val value: T) : Result<T, E>
private data class Err<T,E>(val errors: List<E>) : Result<T, E> {
    constructor(error: E) : this(listOf(error))
}

private fun <T,E> Result<T, E>.getOrExec(default: T, block: (Err<T, E>) -> Unit = {}): T = when (this) {
    is Ok<T, E> -> this.value
    is Err<T, E> -> {
        block(this)
        default
    }
}

@Suppress("unused")
private fun <T,E> Result<T, E>.getOrNull(block: (Err<T, E>) -> Unit = {}): T? = when (this) {
    is Ok<T, E> -> this.value
    is Err<T, E> -> {
        block(this)
        null
    }
}

private fun String?.transportToStubCaseValidated(): Result<Stubs, PartError> = when (this) {
    "success" -> Ok(Stubs.SUCCESS)
    "notFound" -> Ok(Stubs.NOT_FOUND)
    "badId" -> Ok(Stubs.BAD_ID)
    "badName" -> Ok(Stubs.BAD_NAME)
    "badDescription" -> Ok(Stubs.BAD_DESCRIPTION)
    "cannotDelete" -> Ok(Stubs.CANNOT_DELETE)
    "badSearchString" -> Ok(Stubs.BAD_SEARCH_STRING)
    null -> Ok(Stubs.NONE)
    else -> Err(
        PartError(
            code = "wrong-stub-case",
            group = "mapper-validation",
            field = "debug.stub",
            message = "Unsupported value for case \"$this\""
        )
    )
}

@Suppress("unused")
fun AppContext.fromTransportValidated(request: PartCreateRequest) {
    command = Command.CREATE
    // Вся магия здесь!
    stubCase = request
        .debug
        ?.stub
        ?.value
        .transportToStubCaseValidated()
        .getOrExec(Stubs.NONE) { err: Err<Stubs, PartError> ->
            errors.addAll(err.errors)
            state = State.FAILING
        }
}
