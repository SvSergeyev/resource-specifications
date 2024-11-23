package repo

import kotlinx.coroutines.test.runTest
import models.*
import AppContext
import CorSettings
import PartProcessor
import PartRepositoryMock
import models.Part
import models.PartId
import models.PartLock
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val initPart = Part(
    id = PartId("123"),
    name = "abc",
    description = "abc",
    materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
)
private val repo = PartRepositoryMock(
        invokeReadPart = {
            if (it.id == initPart.id) {
                DbPartResponseOk(
                    data = initPart,
                )
            } else errorNotFound(it.id)
        }
    )
private val settings = CorSettings(repoTest = repo)
private val processor = PartProcessor(settings)

fun repoNotFoundTest(command: Command) = runTest {
    val ctx = AppContext(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        partRequest = Part(
            id = PartId("12345"),
            name = "xyz",
            description = "xyz",
            materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
            lock = PartLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(State.FAILING, ctx.state)
    assertEquals(Part(), ctx.partResponse)
    assertEquals(1, ctx.errors.size)
    assertNotNull(ctx.errors.find { it.code == "repo-not-found" }, "Errors must contain not-found")
}
