package mappers

import AppContext
import models.*
import stubs.Stubs
import models.PartError
import tech.sergeyev.education.api.v2.models.*
import kotlin.test.assertEquals
import kotlin.test.Test

class MapperUpdateTest {
    @Test
    fun fromTransport() {
        val req = PartUpdateRequest(
            debug = PartDebug(
                mode = PartRequestDebugMode.STUB,
                stub = PartRequestDebugStubs.SUCCESS,
            ),
            part = PartUpdateObject(
                id = "12345",
                name = "name",
                description = "desc",
                materials = mapOf(Material.STEEL_PLATE_3.description to 0.1),
                lock = "456789",
            ),
        )

        val context = AppContext()
        context.fromTransport(req)

        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(WorkMode.STUB, context.workMode)
        assertEquals("12345", context.partRequest.id.asString())
        assertEquals("456789", context.partRequest.lock.asString())
        assertEquals("name", context.partRequest.name)
        assertEquals(1, context.partRequest.materials.size)
    }

    @Test
    fun toTransport() {
        val context = AppContext(
            requestId = RequestId("1234"),
            command = Command.UPDATE,
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

        val req = context.toTransportPart() as PartUpdateResponse

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
