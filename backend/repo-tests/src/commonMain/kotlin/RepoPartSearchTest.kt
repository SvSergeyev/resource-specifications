import models.Part
import models.UserId
import repo.DbPartFilterRequest
import repo.DbPartsResponseOk
import repo.IRepoPart
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoPartSearchTest {
    abstract val repo: IRepoPart

    protected open val initializedObjects: List<Part> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.searchPart(DbPartFilterRequest(ownerId = searchOwnerId))
        assertIs<DbPartsResponseOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    companion object: BaseInitParts("search") {

        val searchOwnerId = UserId("owner-124")
        override val initObjects: List<Part> = listOf(
            createInitTestModel("ad1"),
            createInitTestModel("ad2", ownerId = searchOwnerId),
            createInitTestModel("ad4", ownerId = searchOwnerId),
        )
    }
}
