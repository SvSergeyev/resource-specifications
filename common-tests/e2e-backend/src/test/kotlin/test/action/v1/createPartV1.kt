package tech.sergeyev.education.e2e.be.test.action.v1

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import tech.sergeyev.education.api.v1.models.PartCreateObject
import tech.sergeyev.education.api.v1.models.PartCreateRequest
import tech.sergeyev.education.api.v1.models.PartCreateResponse
import tech.sergeyev.education.api.v1.models.PartResponseObject

suspend fun Client.createPart(part: PartCreateObject = someCreatePart): PartResponseObject = createPart(part) {
    it should haveSuccessResult
    it.part shouldNotBe null
    it.part?.apply {
        name shouldBe part.name
        description shouldBe part.description
        materials shouldBe part.materials
    }
    it.part!!
}

suspend fun <T> Client.createPart(part: PartCreateObject = someCreatePart, block: (PartCreateResponse) -> T): T =
    withClue("createPartV1: $part") {
        val response = sendAndReceive(
            "parts/create",
            PartCreateRequest(
                requestType = "create",
                debug = debug,
                part = part
            )
        ) as PartCreateResponse

        response.asClue(block)
    }
