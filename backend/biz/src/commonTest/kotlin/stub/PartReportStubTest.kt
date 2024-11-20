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

class PartReportStubTest {

    private val processor = PartProcessor()
    val id = PartId("777")

    @Test
    fun report() = runTest {

        val ctx = AppContext(
            command = Command.REPORT,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            partRequest = Part(
                id = id,
            ),
        )
        processor.exec(ctx)

        assertEquals(id, ctx.partResponse.id)

        with(PartStub.get()) {
            assertEquals(name, ctx.partResponse.name)
            assertEquals(description, ctx.partResponse.description)
            assertEquals(materials, ctx.partResponse.materials)
        }

        assertTrue(ctx.partsResponse.isNotEmpty())
        val first = ctx.partsResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.name.contains(ctx.partResponse.name))
        assertTrue(first.description.contains(ctx.partResponse.description))

        val expectedMaterialsSum = PartStub.get().materials.values.sum()
        val actualMaterialsSum = ctx.partResponse.materials.values.sum()
        assertEquals(expectedMaterialsSum, actualMaterialsSum, "Total material usage mismatch")

    }

    @Test
    fun badId() = runTest {
        val ctx = AppContext(
            command = Command.REPORT,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_ID,
            partRequest = Part(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(Part(), ctx.partResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = AppContext(
            command = Command.REPORT,
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
            command = Command.REPORT,
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
