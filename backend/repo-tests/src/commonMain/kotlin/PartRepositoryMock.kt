import models.Part
import repo.*

class PartRepositoryMock(
    private val invokeCreatePart: (DbPartRequest) -> IDbPartResponse = { DEFAULT_PART_SUCCESS_EMPTY_MOCK },
    private val invokeReadPart: (DbPartIdRequest) -> IDbPartResponse = { DEFAULT_PART_SUCCESS_EMPTY_MOCK },
    private val invokeUpdatePart: (DbPartRequest) -> IDbPartResponse = { DEFAULT_PART_SUCCESS_EMPTY_MOCK },
    private val invokeDeletePart: (DbPartIdRequest) -> IDbPartResponse = { DEFAULT_PART_SUCCESS_EMPTY_MOCK },
    private val invokeSearchPart: (DbPartFilterRequest) -> IDbPartsResponse = { DEFAULT_PARTS_SUCCESS_EMPTY_MOCK },
): IRepoPart {
    override suspend fun createPart(rq: DbPartRequest): IDbPartResponse {
        return invokeCreatePart(rq)
    }

    override suspend fun readPart(rq: DbPartIdRequest): IDbPartResponse {
        return invokeReadPart(rq)
    }

    override suspend fun updatePart(rq: DbPartRequest): IDbPartResponse {
        return invokeUpdatePart(rq)
    }

    override suspend fun deletePart(rq: DbPartIdRequest): IDbPartResponse {
        return invokeDeletePart(rq)
    }

    override suspend fun searchPart(rq: DbPartFilterRequest): IDbPartsResponse {
        return invokeSearchPart(rq)
    }

    companion object {
        val DEFAULT_PART_SUCCESS_EMPTY_MOCK = DbPartResponseOk(Part())
        val DEFAULT_PARTS_SUCCESS_EMPTY_MOCK = DbPartsResponseOk(emptyList())
    }
}
