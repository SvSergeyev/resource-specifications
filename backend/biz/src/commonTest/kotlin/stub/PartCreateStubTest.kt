package stub

import AppContext
import PartProcessor
import PartStub
import kotlinx.coroutines.test.runTest
import models.*
import stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals

class PartCreateStubTest {

    private val processor = PartProcessor()
    val id = PartId("666")
    private val name = "name 666"
    val description = "desc 666"
    private val materials = mapOf(Material.STEEL_PLATE_3 to 0.1)

    @Test
    fun create() = runTest {

        val ctx = AppContext(
            command = Command.CREATE,
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
        assertEquals(PartStub.get().id, ctx.partResponse.id)
        assertEquals(name, ctx.partResponse.name)
        assertEquals(description, ctx.partResponse.description)
        assertEquals(materials, ctx.partResponse.materials)
    }

    @Test
    fun baName() = runTest {
        val ctx = AppContext(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_NAME,
            partRequest = Part(
                id = id,
                name = "",
                description = description,
                materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
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
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_DESCRIPTION,
            partRequest = Part(
                id = id,
                name = name,
                description = "",
                materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
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
            command = Command.CREATE,
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
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_ID,
            partRequest = Part(
                id = id,
                name = name,
                description = description,
                materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
            ),
        )
        processor.exec(ctx)
        assertEquals(Part(), ctx.partResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
