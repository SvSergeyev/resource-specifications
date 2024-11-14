package tech.sergeyev.education.e2e.be.test.action.v1

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import tech.sergeyev.education.api.v1.models.PartReadObject
import tech.sergeyev.education.api.v1.models.PartReportRequest
import tech.sergeyev.education.api.v1.models.PartReportResponse

suspend fun Client.getPartReport(id: String?): PartReportResponse = getPartReport(id) {
    it should haveSuccessResult
    it
}

suspend fun <T> Client.getPartReport(id: String?, block: (PartReportResponse) -> T): T =
    withClue("reportV1: $id") {
        val response = sendAndReceive(
            "parts/report",
            PartReportRequest(
                debug = debug,
                part = PartReadObject(id = id),
            )
        ) as PartReportResponse

        response.asClue(block)
    }
