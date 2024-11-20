package mappers

import AppContext
import stubs.Stubs
import tech.sergeyev.education.api.v2.models.PartCreateRequest
import tech.sergeyev.education.api.v2.models.PartDebug
import tech.sergeyev.education.api.v2.models.PartRequestDebugStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperValidatedTest {
    @Test
    fun fromTransportValidated() {
        val req = PartCreateRequest(
            debug = PartDebug(
                stub = PartRequestDebugStubs.SUCCESS,
            ),
        )

        val context = AppContext()
        context.fromTransportValidated(req)

        assertEquals(Stubs.SUCCESS, context.stubCase)
    }
}
