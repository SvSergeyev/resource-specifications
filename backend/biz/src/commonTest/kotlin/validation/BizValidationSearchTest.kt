package validation

import AppContext
import kotlinx.coroutines.test.runTest
import models.Command
import models.Filter
import models.State
import models.WorkMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizValidationSearchTest: BaseBizValidationTest() {
    override val command = Command.SEARCH

    @Test
    fun correctEmpty() = runTest {
        val ctx = AppContext(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            partFilterRequest = Filter()
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(State.FAILING, ctx.state)
    }
}
