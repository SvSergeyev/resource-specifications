package tech.sergeyev.education.e2e.be.test.action.v1

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldNotBe
import tech.sergeyev.education.api.v1.models.PartReadObject
import tech.sergeyev.education.api.v1.models.PartReadRequest
import tech.sergeyev.education.api.v1.models.PartReadResponse
import tech.sergeyev.education.api.v1.models.PartResponseObject
import tech.sergeyev.education.e2e.be.test.action.beValidId

suspend fun Client.readPart(id: String?): PartResponseObject = readPart(id) {
    it should haveSuccessResult
    it.part shouldNotBe null
    it.part!!
}

suspend fun <T> Client.readPart(id: String?, block: (PartReadResponse) -> T): T =
    withClue("readPartV1: $id") {
        id should beValidId

        val response = sendAndReceive(
            "parts/read",
            PartReadRequest(
                requestType = "read",
                debug = debug,
                part = PartReadObject(id = id)
            )
        ) as PartReadResponse

        response.asClue(block)
    }
