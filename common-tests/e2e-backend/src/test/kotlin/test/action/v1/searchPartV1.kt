package tech.sergeyev.education.e2e.be.test.action.v1

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import tech.sergeyev.education.api.v1.models.PartResponseObject
import tech.sergeyev.education.api.v1.models.PartSearchFilter
import tech.sergeyev.education.api.v1.models.PartSearchRequest
import tech.sergeyev.education.api.v1.models.PartSearchResponse

suspend fun Client.searchPart(search: PartSearchFilter): List<PartResponseObject> = searchPart(search) {
    it should haveSuccessResult
    it.parts ?: listOf()
}

suspend fun <T> Client.searchPart(search: PartSearchFilter, block: (PartSearchResponse) -> T): T =
    withClue("searchPartV1: $search") {
        val response = sendAndReceive(
            "parts/search",
            PartSearchRequest(
                requestType = "search",
                debug = debug,
                filter = search,
            )
        ) as PartSearchResponse

        response.asClue(block)
    }
