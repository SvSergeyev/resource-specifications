package tech.sergeyev.education.e2e.be.test.action.v1

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import tech.sergeyev.education.api.v1.models.PartResponseObject
import tech.sergeyev.education.api.v1.models.PartUpdateObject
import tech.sergeyev.education.api.v1.models.PartUpdateRequest
import tech.sergeyev.education.api.v1.models.PartUpdateResponse
import tech.sergeyev.education.e2e.be.test.action.beValidId
import tech.sergeyev.education.e2e.be.test.action.beValidLock

suspend fun Client.updatePart(part: PartUpdateObject): PartResponseObject =
    updatePart(part) {
        it should haveSuccessResult
        it.part shouldNotBe null
        it.part?.apply {
            if (part.name != null)
                name shouldBe part.name
            if (part.description != null)
                description shouldBe part.description
            if (part.materials != null)
                materials shouldBe part.materials
        }
        it.part!!
    }

suspend fun <T> Client.updatePart(part: PartUpdateObject, block: (PartUpdateResponse) -> T): T {
    val id = part.id
    val lock = part.lock
    return withClue("updatedV1: $id, lock: $lock, set: $part") {
        id should beValidId
        lock should beValidLock

        val response = sendAndReceive(
            "parts/update", PartUpdateRequest(
                debug = debug,
                part = part.copy(id = id, lock = lock)
            )
        ) as PartUpdateResponse

        response.asClue(block)
    }
}
