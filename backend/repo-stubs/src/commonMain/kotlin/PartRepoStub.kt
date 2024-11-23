import repo.*

class PartRepoStub() : IRepoPart {
    override suspend fun createPart(rq: DbPartRequest): IDbPartResponse {
        return DbPartResponseOk(
            data = PartStub.get(),
        )
    }

    override suspend fun readPart(rq: DbPartIdRequest): IDbPartResponse {
        return DbPartResponseOk(
            data = PartStub.get(),
        )
    }

    override suspend fun updatePart(rq: DbPartRequest): IDbPartResponse {
        return DbPartResponseOk(
            data = PartStub.get(),
        )
    }

    override suspend fun deletePart(rq: DbPartIdRequest): IDbPartResponse {
        return DbPartResponseOk(
            data = PartStub.get(),
        )
    }

    override suspend fun searchPart(rq: DbPartFilterRequest): IDbPartsResponse {
        return DbPartsResponseOk(
            data = PartStub.prepareSearchList(filter = ""),
        )
    }
}
