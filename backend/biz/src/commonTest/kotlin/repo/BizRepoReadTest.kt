package repo

import AppContext
import kotlinx.coroutines.test.runTest
import models.*
import CorSettings
import PartProcessor
import PartRepositoryMock
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoReadTest {

    private val userId = UserId("321")
    private val command = Command.READ
    private val initPart = Part(
        id = PartId("123"),
        name = "abc",
        description = "abc",
        ownerId = userId,
        materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
    )
    private val repo = PartRepositoryMock(
        invokeReadPart = {
            DbPartResponseOk(
                data = initPart,
            )
        }
    )
    private val settings = CorSettings(repoTest = repo)
    private val processor = PartProcessor(settings)

    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = AppContext(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            partRequest = Part(
                id = PartId("123"),
            ),
        )
        processor.exec(ctx)
        assertEquals(State.FINISHING, ctx.state)
        assertEquals(initPart.id, ctx.partResponse.id)
        assertEquals(initPart.name, ctx.partResponse.name)
        assertEquals(initPart.description, ctx.partResponse.description)
        assertEquals(initPart.materials, ctx.partResponse.materials)
    }

    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(command)
}
