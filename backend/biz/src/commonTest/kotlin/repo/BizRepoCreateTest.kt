package repo

import AppContext
import CorSettings
import PartProcessor
import PartRepositoryMock
import kotlinx.coroutines.test.runTest
import models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizRepoCreateTest {

    private val userId = UserId("321")
    private val command = Command.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = PartRepositoryMock(
        invokeCreatePart = {
            DbPartResponseOk(
                data = Part(
                    id = PartId(uuid),
                    name = it.part.name,
                    description = it.part.description,
                    ownerId = userId,
                    materials = it.part.materials,
                )
            )
        }
    )
    private val settings = CorSettings(
        repoTest = repo
    )
    private val processor = PartProcessor(settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = AppContext(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            partRequest = Part(
                name = "abc",
                description = "abc",
                materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
            ),
        )
        processor.exec(ctx)
        assertEquals(State.FINISHING, ctx.state)
        assertNotEquals(PartId.NONE, ctx.partResponse.id)
        assertEquals("abc", ctx.partResponse.name)
        assertEquals("abc", ctx.partResponse.description)
        assertEquals(1, ctx.partResponse.materials.size)
    }
}
