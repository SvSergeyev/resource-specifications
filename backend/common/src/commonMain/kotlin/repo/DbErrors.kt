package repo

import helpers.errorSystem
import models.Part
import models.PartId
import models.PartError
import models.PartLock
import repo.exceptions.RepoConcurrencyException
import repo.exceptions.RepoException

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: PartId) = DbPartResponseErr(
    PartError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not Found",
    )
)

val errorEmptyId = DbPartResponseErr(
    PartError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

fun errorRepoConcurrency(
    oldPart: Part,
    expectedLock: PartLock,
    exception: Exception = RepoConcurrencyException(
        id = oldPart.id,
        expectedLock = expectedLock,
        actualLock = oldPart.lock,
    ),
) = DbPartResponseErrWithData(
    part = oldPart,
    err = PartError(
        code = "$ERROR_GROUP_REPO-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldPart.id.asString()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorEmptyLock(id: PartId) = DbPartResponseErr(
    PartError(
        code = "$ERROR_GROUP_REPO-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Part ${id.asString()} is empty that is not admitted"
    )
)

fun errorDb(e: RepoException) = DbPartResponseErr(
    errorSystem(
        violationCode = "dbLockEmpty",
        e = e
    )
)
