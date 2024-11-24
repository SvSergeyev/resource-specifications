package repo

import AppContext
import models.*
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import tech.sergeyev.education.api.v1.models.*
import toTransportCreate
import toTransportDelete
import toTransportRead
import toTransportReport
import toTransportSearch
import toTransportUpdate
import kotlin.test.Test

internal abstract class PartRepoBaseV1Test {
    protected abstract var webClient: WebTestClient
    private val debug = PartDebug(mode = PartRequestDebugMode.TEST)
    protected val uuidNew = "10000000-0000-0000-0000-000000000001"

    @Test
    open fun createPart() = testRepoPart(
        "create",
        PartCreateRequest(
            part = PartStub.get().toTransportCreate(),
            debug = debug,
        ),
        prepareCtx(PartStub.prepareResult {
            id = PartId(uuidNew)
            ownerId = UserId.NONE
            lock = PartLock(uuidNew)
        })
            .toTransportCreate()
            .copy(responseType = "create")
    )

    @Test
    open fun readPart() = testRepoPart(
        "read",
        PartReadRequest(
            part = PartStub.get().toTransportRead(),
            debug = debug,
        ),
        prepareCtx(PartStub.get())
            .toTransportRead()
            .copy(responseType = "read")
    )

    @Test
    open fun updatePart() = testRepoPart(
        "update",
        PartUpdateRequest(
            part = PartStub.prepareResult { name = "add" }.toTransportUpdate(),
            debug = debug,
        ),
        prepareCtx(PartStub.prepareResult { name = "add"; lock = PartLock(uuidNew) } )
            .toTransportUpdate().copy(responseType = "update")
    )

    @Test
    open fun deletePart() = testRepoPart(
        "delete",
        PartDeleteRequest(
            part = PartStub.get().toTransportDelete(),
            debug = debug,
        ),
        prepareCtx(PartStub.get())
            .toTransportDelete()
            .copy(responseType = "delete")
    )

    @Test
    open fun searchPart() = testRepoPart(
        "search",
        PartSearchRequest(
            filter = PartSearchFilter(),
            debug = debug,
        ),
        AppContext(
            state = State.RUNNING,
            partsResponse = PartStub.prepareSearchList("xx")
                .sortedBy { it.id.asString() }
                .toMutableList()
        )
            .toTransportSearch().copy(responseType = "search")
    )

    @Test
    open fun reportPart() = testRepoPart(
        "report",
        PartReportRequest(
            part = PartStub.get().toTransportRead(),
            debug = debug,
        ),
        AppContext(
            state = State.RUNNING,
            partResponse = PartStub.get(),
        )
            .toTransportReport().copy(responseType = "report")
    )

    private fun prepareCtx(part: Part) = AppContext(
        state = State.RUNNING,
        partResponse = part.apply {
            // Пока не реализована эта функциональность
        },
    )

    private inline fun <reified Req : Any, reified Res : IResponse> testRepoPart(
        url: String,
        requestObj: Req,
        expectObj: Res,
    ) {
        webClient
            .post()
            .uri("/v1/parts/$url")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                val sortedResp: IResponse = when (it) {
                    is PartSearchResponse -> it.copy(parts = it.parts?.sortedBy { it.id })
                    else -> it
                }
                assertThat(sortedResp).isEqualTo(expectObj)
            }
    }
}
