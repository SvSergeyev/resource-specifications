import models.*
import org.junit.Test
import stubs.Stubs
import tech.sergeyev.education.api.v1.models.*
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req = PartCreateRequest(
            debug = PartDebug(
                mode = PartRequestDebugMode.STUB,
                stub = PartRequestDebugStubs.SUCCESS,
            ),
            part = PartCreateObject(
                name = "name",
                description = "desc",
                materials = mapOf(Material.STEEL_PLATE_3.name to 0.25)
            ),
        )

        val context = AppContext()
        context.fromTransport(req)

        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(WorkMode.STUB, context.workMode)
        assertEquals("name", context.partRequest.name)
        assertEquals("desc", context.partRequest.description)
        assertEquals(1, context.partRequest.materials.size)
    }

    @Test
    fun toTransport() {
        val context = AppContext(
            requestId = RequestId("1234"),
            command = Command.CREATE,
            partResponse = Part(
                name = "name",
                description = "desc",
                materials = mapOf(Material.STEEL_PLATE_3 to 0.25)
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

        val req = context.toTransportPart() as PartCreateResponse

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
