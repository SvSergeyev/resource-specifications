import com.benasher44.uuid.uuid4
import models.Part
import repo.*

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class RepoPartSql(
    properties: SqlProperties,
    randomUuid: () -> String = { uuid4().toString() },
) : IRepoPart, IRepoPartInitializable {
    override fun save(parts: Collection<Part>): Collection<Part>
    override suspend fun createPart(rq: DbPartRequest): IDbPartResponse
    override suspend fun readPart(rq: DbPartIdRequest): IDbPartResponse
    override suspend fun updatePart(rq: DbPartRequest): IDbPartResponse
    override suspend fun deletePart(rq: DbPartIdRequest): IDbPartResponse
    override suspend fun searchPart(rq: DbPartFilterRequest): IDbPartsResponse
    fun clear()
}
