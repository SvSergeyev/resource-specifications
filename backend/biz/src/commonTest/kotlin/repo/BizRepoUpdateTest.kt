package repo

import AppContext
import CorSettings
import PartProcessor
import PartRepositoryMock
import kotlinx.coroutines.test.runTest
import models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoUpdateTest {

    private val userId = UserId("321")
    private val command = Command.UPDATE
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
        invokeUpdatePart = {
            DbPartResponseOk(
                data = Part(
                    id = PartId("123"),
                    name = "xyz",
                    description = "xyz",
                    materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
                    lock = PartLock("123-234-abc-ABC"),
                )
            )
        }
    )
    private val settings = CorSettings(repoTest = repo)
    private val processor = PartProcessor(settings)

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val partToUpdate = Part(
            id = PartId("123"),
            name = "xyz",
            description = "xyz",
            materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
            lock = PartLock("123-234-abc-ABC"),
        )
        val ctx = AppContext(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            partRequest = partToUpdate,
        )
        processor.exec(ctx)
        assertEquals(State.FINISHING, ctx.state)
        assertEquals(partToUpdate.id, ctx.partResponse.id)
        assertEquals(partToUpdate.name, ctx.partResponse.name)
        assertEquals(partToUpdate.description, ctx.partResponse.description)
        assertEquals(partToUpdate.materials, ctx.partResponse.materials)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}
