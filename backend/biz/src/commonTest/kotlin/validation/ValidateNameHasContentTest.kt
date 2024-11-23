package validation

import AppContext
import kotlinx.coroutines.test.runTest
import models.Filter
import models.Part
import models.State
import tech.sergeyev.education.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateNameHasContentTest {
    @Test
    fun emptyString() = runTest {
        val ctx = AppContext(state = State.RUNNING, partValidating = Part(name = ""))
        chain.exec(ctx)
        assertEquals(State.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun noContent() = runTest {
        val ctx = AppContext(state = State.FAILING, partValidating = Part(name = "12!@#$%^&*()_+-="))
        chain.exec(ctx)
        assertEquals(State.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-name-noContent", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = AppContext(state = State.RUNNING, partFilterValidating = Filter(searchString = "Ж"))
        chain.exec(ctx)
        assertEquals(State.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validateSearchStringLength("")
        }.build()
    }
}
