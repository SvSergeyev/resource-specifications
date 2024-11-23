package validation

import AppContext
import PartProcessor
import kotlinx.coroutines.test.runTest
import models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = PartStub.get()

fun validationDescriptionCorrect(command: Command, processor: PartProcessor) = runTest {
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
    assertEquals("abc", ctx.partValidated.description)
}

fun validationDescriptionTrim(command: Command, processor: PartProcessor) = runTest {
    val ctx = AppContext(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        partRequest = Part(
            id = stub.id,
            name = "abc",
            description = " \n\tabc \n\t",
            materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
            lock = PartLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
    assertEquals("abc", ctx.partValidated.description)
}

fun validationDescriptionEmpty(command: Command, processor: PartProcessor) = runTest {
    val ctx = AppContext(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        partRequest = Part(
            id = stub.id,
            name = "abc",
            description = "",
            materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
            lock = PartLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}

fun validationDescriptionSymbols(command: Command, processor: PartProcessor) = runTest {
    val ctx = AppContext(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        partRequest = Part(
            id = stub.id,
            name = "abc",
            description = "!@#$%^&*(),.{}",
            materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
            lock = PartLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}
