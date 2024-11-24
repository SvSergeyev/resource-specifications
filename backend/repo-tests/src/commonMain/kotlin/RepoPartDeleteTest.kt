import models.Part
import models.PartId
import repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class RepoPartDeleteTest {
    abstract val repo: IRepoPart
    protected open val deleteSucc = initObjects[0]
    protected open val deleteConc = initObjects[1]
    protected open val notFoundId = PartId("part-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSucc.lock
        val result = repo.deletePart(DbPartIdRequest(deleteSucc.id, lock = lockOld))
        assertIs<DbPartResponseOk>(result)
        assertEquals(deleteSucc.name, result.data.name)
        assertEquals(deleteSucc.description, result.data.description)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readPart(DbPartIdRequest(notFoundId, lock = lockOld))

        assertIs<DbPartResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val result = repo.deletePart(DbPartIdRequest(deleteConc.id, lock = lockBad))

        assertIs<DbPartResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }

    companion object : BaseInitParts("delete") {
        override val initObjects: List<Part> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}
