import kotlinx.coroutines.test.runTest
import models.Part
import repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PartRepositoryMockTest {
    private val repo = PartRepositoryMock(
        invokeCreatePart = { DbPartResponseOk(PartStub.prepareResult { name = "create" }) },
        invokeReadPart = { DbPartResponseOk(PartStub.prepareResult { name = "read" }) },
        invokeUpdatePart = { DbPartResponseOk(PartStub.prepareResult { name = "update" }) },
        invokeDeletePart = { DbPartResponseOk(PartStub.prepareResult { name = "delete" }) },
        invokeSearchPart = { DbPartsResponseOk(listOf(PartStub.prepareResult { name = "search" })) },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createPart(DbPartRequest(Part()))
        assertIs<DbPartResponseOk>(result)
        assertEquals("create", result.data.name)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readPart(DbPartIdRequest(Part()))
        assertIs<DbPartResponseOk>(result)
        assertEquals("read", result.data.name)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updatePart(DbPartRequest(Part()))
        assertIs<DbPartResponseOk>(result)
        assertEquals("update", result.data.name)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deletePart(DbPartIdRequest(Part()))
        assertIs<DbPartResponseOk>(result)
        assertEquals("delete", result.data.name)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchPart(DbPartFilterRequest())
        assertIs<DbPartsResponseOk>(result)
        assertEquals("search", result.data.first().name)
    }
}
