package repo

import AppContext
import CorSettings
import PartProcessor
import kotlinx.coroutines.test.runTest
import models.*
import PartRepositoryMock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BizRepoDeleteTest {

    private val userId = UserId("321")
    private val command = Command.DELETE
    private val initPart = Part(
        id = PartId("123"),
        name = "abc",
        description = "abc",
        ownerId = userId,
        materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
        lock = PartLock("123-234-abc-ABC"),
    )
    private val repo = PartRepositoryMock(
        invokeReadPart = {
            DbPartResponseOk(
                data = initPart,
            )
        },
        invokeDeletePart = {
            if (it.id == initPart.id)
                DbPartResponseOk(
                    data = initPart
                )
            else DbPartResponseErr()
        }
    )
    private val settings by lazy {
        CorSettings(
            repoTest = repo
        )
    }
    private val processor = PartProcessor(settings)

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val partToUpdate = Part(
            id = PartId("123"),
            lock = PartLock("123"),
        )
        val ctx = AppContext(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            partRequest = partToUpdate,
        )
        processor.exec(ctx)
        assertEquals(State.FINISHING, ctx.state)
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initPart.id, ctx.partResponse.id)
        assertEquals(initPart.name, ctx.partResponse.name)
        assertEquals(initPart.description, ctx.partResponse.description)
        assertEquals(initPart.materials, ctx.partResponse.materials)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}
