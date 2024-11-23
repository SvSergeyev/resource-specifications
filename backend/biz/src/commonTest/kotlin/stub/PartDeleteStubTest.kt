package stub

import AppContext
import PartProcessor
import kotlinx.coroutines.test.runTest
import models.*
import stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals

class PartDeleteStubTest {

    private val processor = PartProcessor()
    val id = PartId("666")

    @Test
    fun delete() = runTest {

        val ctx = AppContext(
            command = Command.DELETE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            partRequest = Part(
                id = id,
            ),
        )
        processor.exec(ctx)

        val stub = PartStub.get()
        assertEquals(stub.id, ctx.partResponse.id)
        assertEquals(stub.name, ctx.partResponse.name)
        assertEquals(stub.description, ctx.partResponse.description)
        assertEquals(stub.materials, ctx.partResponse.materials)
    }

    @Test
    fun badId() = runTest {
        val ctx = AppContext(
            command = Command.DELETE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_ID,
            partRequest = Part(),
        )
        processor.exec(ctx)
        assertEquals(Part(), ctx.partResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = AppContext(
            command = Command.DELETE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.DB_ERROR,
            partRequest = Part(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(Part(), ctx.partResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = AppContext(
            command = Command.DELETE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_NAME,
            partRequest = Part(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(Part(), ctx.partResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
