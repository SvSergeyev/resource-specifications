import models.Material
import models.Part
import models.PartId
import models.UserId
import repo.DbPartRequest
import repo.DbPartResponseOk
import kotlin.test.*

abstract class RepoPartCreateTest {
    abstract val repo: IRepoPartInitializable
    protected open val uuidNew = PartId("10000000-0000-0000-0000-000000000001")

    private val createObj = Part(
        name = "create object",
        description = "create object description",
        ownerId = UserId("owner-123"),
        materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createPart(DbPartRequest(createObj))
        val expected = createObj
        assertIs<DbPartResponseOk>(result)
        assertEquals(uuidNew, result.data.id)
        assertEquals(expected.name, result.data.name)
        assertEquals(expected.description, result.data.description)
        assertEquals(expected.materials, result.data.materials)
    }

    companion object : BaseInitParts("create") {
        override val initObjects: List<Part> = emptyList()
    }
}
