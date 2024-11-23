package repo

import AppContext
import kotlinx.coroutines.test.runTest
import models.*
import CorSettings
import PartProcessor
import PartRepositoryMock
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoSearchTest {

    private val userId = UserId("321")
    private val command = Command.SEARCH
    private val initPart = Part(
        id = PartId("123"),
        name = "abc",
        description = "abc",
        ownerId = userId,
        materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
    )
    private val repo = PartRepositoryMock(
        invokeSearchPart = {
            DbPartsResponseOk(
                data = listOf(initPart),
            )
        }
    )
    private val settings = CorSettings(repoTest = repo)
    private val processor = PartProcessor(settings)

    @Test
    fun repoSearchSuccessTest() = runTest {
        val ctx = AppContext(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            partFilterRequest = Filter(
                searchString = "abc",
            ),
        )
        processor.exec(ctx)
        assertEquals(State.FINISHING, ctx.state)
        assertEquals(1, ctx.partsResponse.size)
    }
}
