package tech.sergeyev.education.app.spring.mock

import AppContext
import PartProcessor
import PartStub
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.any
import org.mockito.kotlin.wheneverBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
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

// Temporary simple test with general.stubs.stubs
@WebFluxTest(PartControllerV1Fine::class, PartConfig::class)
internal class PartControllerV1MockTest {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockBean
    private lateinit var processor: PartProcessor

    @BeforeEach
    fun tearUp() {
        wheneverBlocking { processor.exec(any()) }.then {
            it.getArgument<AppContext>(0).apply {
                partResponse = PartStub.get()
                partsResponse = PartStub.prepareSearchList("sdf").toMutableList()
            }
        }
    }

    @Test
    fun createPart() = testStubPart(
        "/v1/parts/create",
        PartCreateRequest(),
        AppContext(partResponse = PartStub.get()).toTransportCreate().copy(responseType = "create")
    )

    @Test
    fun readPart() = testStubPart(
        "/v1/parts/read",
        PartReadRequest(),
        AppContext(partResponse = PartStub.get()).toTransportRead().copy(responseType = "read")
    )

    @Test
    fun updatePart() = testStubPart(
        "/v1/parts/update",
        PartUpdateRequest(),
        AppContext(partResponse = PartStub.get()).toTransportUpdate().copy(responseType = "update")
    )

    @Test
    fun deletePart() = testStubPart(
        "/v1/parts/delete",
        PartDeleteRequest(),
        AppContext(partResponse = PartStub.get()).toTransportDelete().copy(responseType = "delete")
    )

    @Test
    fun searchPart() = testStubPart(
        "/v1/parts/search",
        PartSearchRequest(),
        AppContext(partsResponse = PartStub.prepareSearchList("sdf").toMutableList())
            .toTransportSearch().copy(responseType = "search")
    )

    @Test
    fun partReport() = testStubPart(
        "/v1/parts/report",
        PartReportRequest(),
        AppContext(partResponse = PartStub.prepareReport(PartStub.get()))
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
