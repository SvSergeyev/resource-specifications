package stub

import AppContext
import PartProcessor
import kotlinx.coroutines.test.runTest
import models.*
import stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals

class PartUpdateStubTest {

    private val processor = PartProcessor()
    val id = PartId("777")
    private val name = "name 666"
    val description = "desc 666"
    private val materials = mapOf(Material.STEEL_PLATE_3 to 0.1)

    @Test
    fun create() = runTest {

        val ctx = AppContext(
            command = Command.UPDATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            partRequest = Part(
                id = id,
                name = name,
                description = description,
                materials = materials,
            ),
        )
        processor.exec(ctx)
        assertEquals(id, ctx.partResponse.id)
        assertEquals(name, ctx.partResponse.name)
        assertEquals(description, ctx.partResponse.description)
        assertEquals(materials, ctx.partResponse.materials)
    }

    @Test
    fun badId() = runTest {
        val ctx = AppContext(
            command = Command.UPDATE,
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
    fun badTitle() = runTest {
        val ctx = AppContext(
            command = Command.UPDATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_NAME,
            partRequest = Part(
                id = id,
                name = "",
                description = description,
                materials = materials,
            ),
        )
        processor.exec(ctx)
        assertEquals(Part(), ctx.partResponse)
        assertEquals("name", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
    @Test
    fun badDescription() = runTest {
        val ctx = AppContext(
            command = Command.UPDATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_DESCRIPTION,
            partRequest = Part(
                id = id,
                name = name,
                description = "",
                materials = materials,
            ),
        )
        processor.exec(ctx)
        assertEquals(Part(), ctx.partResponse)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = AppContext(
            command = Command.UPDATE,
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
            command = Command.UPDATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_SEARCH_STRING,
            partRequest = Part(
                id = id,
                name = name,
                description = description,
                materials = materials,
            ),
        )
        processor.exec(ctx)
        assertEquals(Part(), ctx.partResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
