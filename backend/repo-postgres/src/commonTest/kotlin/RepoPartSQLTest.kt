import repo.IRepoPart
import kotlin.test.AfterTest

private fun IRepoPart.clear() {
    val pgRepo = (this as PartRepoInitialized).repo as RepoPartSql
    pgRepo.clear()
}

class RepoPartSQLCreateTest : RepoPartCreateTest() {
    override val repo: IRepoPartInitializable = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { uuidNew.asString() },
    )
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoPartSQLReadTest : RepoPartReadTest() {
    override val repo: IRepoPart = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoAdSQLUpdateTest : RepoPartUpdateTest() {
    override val repo: IRepoPart = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )
    @AfterTest
    fun tearDown() {
        repo.clear()
    }
}

class RepoAdSQLDeleteTest : RepoPartDeleteTest() {
    override val repo: IRepoPart = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoAdSQLSearchTest : RepoPartSearchTest() {
    override val repo: IRepoPart = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}
