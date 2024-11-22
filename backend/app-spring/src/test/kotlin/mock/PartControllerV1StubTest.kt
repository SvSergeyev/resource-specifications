package tech.sergeyev.education.app.spring.mock

import AppContext
import models.State
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import tech.sergeyev.education.api.v1.models.*
import tech.sergeyev.education.app.spring.config.PartConfig
import tech.sergeyev.education.app.spring.controllers.PartControllerV1Fine
import toTransportCreate
import toTransportDelete
import toTransportRead
import toTransportReport
import toTransportSearch
import toTransportUpdate
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(PartControllerV1Fine::class, PartConfig::class)
internal class PartControllerV1StubTest {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Test
    fun createPart() = testStubPart(
        "/v1/parts/create",
        PartCreateRequest(),
        AppContext(partResponse = PartStub.get(), state = State.FINISHING)
            .toTransportCreate().copy(responseType = "create")
    )

    @Test
    fun readPart() = testStubPart(
        "/v1/parts/read",
        PartReadRequest(),
        AppContext(partResponse = PartStub.get(), state = State.FINISHING)
            .toTransportRead().copy(responseType = "read")
    )

    @Test
    fun updatePart() = testStubPart(
        "/v1/parts/update",
        PartUpdateRequest(),
        AppContext(partResponse = PartStub.get(), state = State.FINISHING)
            .toTransportUpdate().copy(responseType = "update")
    )

    @Test
    fun deletePart() = testStubPart(
        "/v1/parts/delete",
        PartDeleteRequest(),
        AppContext(partResponse = PartStub.get(), state = State.FINISHING)
            .toTransportDelete().copy(responseType = "delete")
    )

    @Test
    fun searchPart() = testStubPart(
        "/v1/parts/search",
        PartSearchRequest(),
        AppContext(
            partsResponse = PartStub.prepareSearchList("plate").toMutableList(),
            state = State.FINISHING
        )
            .toTransportSearch().copy(responseType = "search")
    )

    @Test
    fun reportPart() = testStubPart(
        "/v1/parts/report",
        PartReportRequest(),
        AppContext(
            partResponse = PartStub.prepareReport(),
            state = State.FINISHING
        )
            .toTransportReport().copy(responseType = "report")
    )

    private inline fun <reified Req : Any, reified Res : Any> testStubPart(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                assertThat(it).isEqualTo(responseObj)
            }
    }
}
