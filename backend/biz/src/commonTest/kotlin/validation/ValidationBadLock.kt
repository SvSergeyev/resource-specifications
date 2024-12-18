package validation

import AppContext
import PartProcessor
import kotlinx.coroutines.test.runTest
import models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationLockCorrect(command: Command, processor: PartProcessor) = runTest {
    val ctx = AppContext(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        partRequest = Part(
            id = PartId("123-234-abc-ABC"),
            name = "abc",
            description = "abc",
            materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
            lock = PartLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
}

fun validationLockTrim(command: Command, processor: PartProcessor) = runTest {
    val ctx = AppContext(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        partRequest = Part(
            id = PartId("123-234-abc-ABC"),
            name = "abc",
            description = "abc",
            materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
            lock = PartLock(" \n\t 123-234-abc-ABC \n\t "),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
}

fun validationLockEmpty(command: Command, processor: PartProcessor) = runTest {
    val ctx = AppContext(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        partRequest = Part(
            id = PartId("123-234-abc-ABC"),
            name = "abc",
            description = "abc",
            materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
            lock = PartLock(""),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationLockFormat(command: Command, processor: PartProcessor) = runTest {
    val ctx = AppContext(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        partRequest = Part(
            id = PartId("123-234-abc-ABC"),
            name = "abc",
            description = "abc",
            materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
            lock = PartLock("!@#\$%^&*(),.{}"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}
