import models.PartId
import models.Part
import repo.DbPartIdRequest
import repo.DbPartResponseErr
import repo.DbPartResponseOk
import repo.IRepoPart
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoPartReadTest {
    abstract val repo: IRepoPart
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readPart(DbPartIdRequest(readSucc.id))

        assertIs<DbPartResponseOk>(result)
        assertEquals(readSucc, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readPart(DbPartIdRequest(notFoundId))

        assertIs<DbPartResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitParts("delete") {
        override val initObjects: List<Part> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = PartId("part-repo-read-notFound")

    }
}
