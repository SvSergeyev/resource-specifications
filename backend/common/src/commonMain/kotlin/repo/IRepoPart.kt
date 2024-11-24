package repo
interface IRepoPart {
    suspend fun createPart(rq: DbPartRequest): IDbPartResponse
    suspend fun readPart(rq: DbPartIdRequest): IDbPartResponse
    suspend fun updatePart(rq: DbPartRequest): IDbPartResponse
    suspend fun deletePart(rq: DbPartIdRequest): IDbPartResponse
    suspend fun searchPart(rq: DbPartFilterRequest): IDbPartsResponse
    companion object {
        val NONE = object : IRepoPart {
            override suspend fun createPart(rq: DbPartRequest): IDbPartResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readPart(rq: DbPartIdRequest): IDbPartResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updatePart(rq: DbPartRequest): IDbPartResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deletePart(rq: DbPartIdRequest): IDbPartResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchPart(rq: DbPartFilterRequest): IDbPartsResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}
