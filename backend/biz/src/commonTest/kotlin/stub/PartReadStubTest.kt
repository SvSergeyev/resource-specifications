package stub

import AppContext
import PartProcessor
import kotlinx.coroutines.test.runTest
import models.*
import stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals

class PartReadStubTest {

    private val processor = PartProcessor()
    val id = PartId("666")

    @Test
    fun read() = runTest {

        val ctx = AppContext(
            command = Command.READ,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            partRequest = Part(
                id = id,
            ),
        )
        processor.exec(ctx)
        with (PartStub.get()) {
            assertEquals(id, ctx.partResponse.id)
            assertEquals(name, ctx.partResponse.name)
            assertEquals(description, ctx.partResponse.description)
            assertEquals(materials, ctx.partResponse.materials)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = AppContext(
            command = Command.READ,
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
            command = Command.READ,
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
            command = Command.READ,
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
