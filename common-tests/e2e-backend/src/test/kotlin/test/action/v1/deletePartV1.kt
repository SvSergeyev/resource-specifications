package tech.sergeyev.education.e2e.be.test.action.v1

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import tech.sergeyev.education.api.v1.models.PartDeleteObject
import tech.sergeyev.education.api.v1.models.PartDeleteRequest
import tech.sergeyev.education.api.v1.models.PartDeleteResponse
import tech.sergeyev.education.api.v1.models.PartResponseObject
import tech.sergeyev.education.e2e.be.test.action.beValidId
import tech.sergeyev.education.e2e.be.test.action.beValidLock

suspend fun Client.deletePart(part: PartResponseObject) {
    val id = part.id
    val lock = part.lock
    withClue("deletePartV1: $id, lock: $lock") {
        id should beValidId
        lock should beValidLock

        val response = sendAndReceive(
            "parts/delete",
            PartDeleteRequest(
                debug = debug,
                part = PartDeleteObject(id = id, lock = lock)
            )
        ) as PartDeleteResponse

        response.asClue {
            response should haveSuccessResult
            response.part shouldBe part
        }
    }
}
