package stub

import AppContext
import PartProcessor
import kotlinx.coroutines.test.runTest
import models.*
import stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class PartSearchStubTest {

    private val processor = PartProcessor()
    private val filter = Filter(searchString = "plate")

    @Test
    fun read() = runTest {

        val ctx = AppContext(
            command = Command.SEARCH,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            partFilterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.partsResponse.size > 1)
        val first = ctx.partsResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.name.contains(filter.searchString))
        assertTrue(first.description.contains(filter.searchString))
        with (PartStub.get()) {
            assertEquals(materials, first.materials)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = AppContext(
            command = Command.SEARCH,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_ID,
            partFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(Part(), ctx.partResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = AppContext(
            command = Command.SEARCH,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.DB_ERROR,
            partFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(Part(), ctx.partResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = AppContext(
            command = Command.SEARCH,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_NAME,
            partFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(Part(), ctx.partResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
