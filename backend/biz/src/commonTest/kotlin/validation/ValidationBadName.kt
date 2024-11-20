package validation

import AppContext
import PartProcessor
import PartStub
import kotlinx.coroutines.test.runTest
import models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = PartStub.get()

fun validationNameCorrect(command: Command, processor: PartProcessor) = runTest {
    val ctx = AppContext(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        partRequest = Part(
            id = stub.id,
            name = "abc",
            description = "abc",
            materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
            lock = PartLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
    assertEquals("abc", ctx.partValidated.name)
}

fun validationNameTrim(command: Command, processor: PartProcessor) = runTest {
    val ctx = AppContext(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        partRequest = Part(
            id = stub.id,
            name = " \n\t abc \t\n ",
            description = "abc",
            materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
            lock = PartLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
    assertEquals("abc", ctx.partValidated.name)
}

fun validationNameEmpty(command: Command, processor: PartProcessor) = runTest {
    val ctx = AppContext(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        partRequest = Part(
            id = stub.id,
            name = "",
            description = "abc",
            materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
            lock = PartLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("name", error?.field)
    assertContains(error?.message ?: "", "name")
}

fun validationNameSymbols(command: Command, processor: PartProcessor) = runTest {
    val ctx = AppContext(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        partRequest = Part(
            id = PartId("123"),
            name = "!@#$%^&*(),.{}",
            description = "abc",
            materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
            lock = PartLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("name", error?.field)
    assertContains(error?.message ?: "", "name")
}
