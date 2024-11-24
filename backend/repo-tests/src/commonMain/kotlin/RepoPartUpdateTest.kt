import models.*
import repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoPartUpdateTest {
    abstract val repo: IRepoPart
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = PartId("part-repo-update-not-found")
    protected val lockBad = PartLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = PartLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        Part(
            id = updateSucc.id,
            name = "update object",
            description = "update object description",
            ownerId = UserId("owner-123"),
            materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
            lock = initObjects.first().lock,
        )
    }
    private val reqUpdateNotFound = Part(
        id = updateIdNotFound,
        name = "update object not found",
        description = "update object not found description",
        ownerId = UserId("owner-123"),
        materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
        lock = initObjects.first().lock,
    )

    private val reqUpdateConc by lazy {
        Part(
            id = updateConc.id,
            name = "update object not found",
            description = "update object not found description",
            ownerId = UserId("owner-123"),
            materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updatePart(DbPartRequest(reqUpdateSucc))
        assertIs<DbPartResponseOk>(result)
        assertEquals(reqUpdateSucc.id, result.data.id)
        assertEquals(reqUpdateSucc.name, result.data.name)
        assertEquals(reqUpdateSucc.description, result.data.description)
        assertEquals(reqUpdateSucc.materials, result.data.materials)
        assertEquals(lockNew, result.data.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updatePart(DbPartRequest(reqUpdateNotFound))
        assertIs<DbPartResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updatePart(DbPartRequest(reqUpdateConc))
        assertIs<DbPartResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitParts("update") {
        override val initObjects: List<Part> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}
