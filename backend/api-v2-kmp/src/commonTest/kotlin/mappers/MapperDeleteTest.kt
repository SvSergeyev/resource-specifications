package mappers

import AppContext
import mappers.fromTransport
import models.*
import stubs.Stubs
import tech.sergeyev.education.api.v1.models.PartError
import tech.sergeyev.education.api.v2.models.*
import kotlin.test.assertEquals
import kotlin.test.Test

class MapperDeleteTest {
    @Test
    fun fromTransport() {
        val req = PartDeleteRequest(
            debug = PartDebug(
                mode = PartRequestDebugMode.STUB,
                stub = PartRequestDebugStubs.SUCCESS,
            ),
            part = PartDeleteObject(
                id = "12345",
                lock = "456789",
            ),
        )

        val context = AppContext()
        context.fromTransport(req)

        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(WorkMode.STUB, context.workMode)
        assertEquals("12345", context.partRequest.id.asString())
        assertEquals("456789", context.partRequest.lock.asString())
    }

    @Test
    fun toTransport() {
        val context = AppContext(
            requestId = RequestId("1234"),
            command = Command.DELETE,
            partResponse = Part(
                id = PartId("12345"),
                name = "name",
                description = "desc",
                materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
                lock = PartLock("456789"),
            ),
            errors = mutableListOf(
                PartError(
                    code = "err",
                    group = "request",
                    field = "name",
                    message = "wrong name",
                )
            ),
            state = State.RUNNING,
        )

        val req = context.toTransportPart() as PartDeleteResponse

        assertEquals("12345", req.part?.id)
        assertEquals("456789", req.part?.lock)
        assertEquals("name", req.part?.name)
        assertEquals("desc", req.part?.description)
        assertEquals(1, req.part?.materials?.size)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("name", req.errors?.firstOrNull()?.field)
        assertEquals("wrong name", req.errors?.firstOrNull()?.message)
    }
}
