
class PartRepoInMemoryCreateTest : RepoPartCreateTest() {
    override val repo = PartRepoInitialized(
        PartRepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}

class PartRepoInMemoryDeleteTest : RepoPartDeleteTest() {
    override val repo = PartRepoInitialized(
        PartRepoInMemory(),
        initObjects = initObjects,
    )
}

class PartRepoInMemoryReadTest : RepoPartReadTest() {
    override val repo = PartRepoInitialized(
        PartRepoInMemory(),
        initObjects = initObjects,
    )
}

class PartRepoInMemorySearchTest : RepoPartSearchTest() {
    override val repo = PartRepoInitialized(
        PartRepoInMemory(),
        initObjects = initObjects,
    )
}

class PartRepoInMemoryUpdateTest : RepoPartUpdateTest() {
    override val repo = PartRepoInitialized(
        PartRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}
