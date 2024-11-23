package validation

import CorSettings
import PartProcessor
import PartRepoInMemory
import PartRepoInitialized
import models.Command

abstract class BaseBizValidationTest {
    protected abstract val command: Command
    private val repo = PartRepoInitialized(
        repo = PartRepoInMemory(),
        initObjects = listOf(
            PartStub.get(),
        ),
    )
    private val settings by lazy { CorSettings() }
    protected val processor by lazy { PartProcessor(settings) }
}
