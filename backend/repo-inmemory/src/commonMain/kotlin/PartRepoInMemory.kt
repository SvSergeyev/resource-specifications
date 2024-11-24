
import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import models.Part
import models.PartId
import models.PartLock
import models.UserId
import repo.*
import repo.exceptions.RepoEmptyLockException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class PartRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : PartRepoBase(), IRepoPart, IRepoPartInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, PartEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(parts: Collection<Part>) = parts.map { part ->
        val entity = PartEntity(part)
        require(entity.id != null)
        cache.put(entity.id, entity)
        part
    }

    override suspend fun createPart(rq: DbPartRequest): IDbPartResponse = tryPartMethod {
        val key = randomUuid()
        val part = rq.part.copy(id = PartId(key), lock = PartLock(randomUuid()))
        val entity = PartEntity(part)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbPartResponseOk(part)
    }

    override suspend fun readPart(rq: DbPartIdRequest): IDbPartResponse = tryPartMethod {
        val key = rq.id.takeIf { it != PartId.NONE }?.asString() ?: return@tryPartMethod errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {
                    DbPartResponseOk(it.toInternal())
                } ?: errorNotFound(rq.id)
        }
    }

    override suspend fun updatePart(rq: DbPartRequest): IDbPartResponse = tryPartMethod {
        val rqPart = rq.part
        val id = rqPart.id.takeIf { it != PartId.NONE } ?: return@tryPartMethod errorEmptyId
        val key = id.asString()
        val oldLock = rqPart.lock.takeIf { it != PartLock.NONE } ?: return@tryPartMethod errorEmptyLock(id)

        mutex.withLock {
            val oldPart = cache.get(key)?.toInternal()
            when {
                oldPart == null -> errorNotFound(id)
                oldPart.lock == PartLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldPart.lock != oldLock -> errorRepoConcurrency(oldPart, oldLock)
                else -> {
                    val newPart = rqPart.copy(lock = PartLock(randomUuid()))
                    val entity = PartEntity(newPart)
                    cache.put(key, entity)
                    DbPartResponseOk(newPart)
                }
            }
        }
    }


    override suspend fun deletePart(rq: DbPartIdRequest): IDbPartResponse = tryPartMethod {
        val id = rq.id.takeIf { it != PartId.NONE } ?: return@tryPartMethod errorEmptyId
        val key = id.asString()
        val oldLock = rq.lock.takeIf { it != PartLock.NONE } ?: return@tryPartMethod errorEmptyLock(id)

        mutex.withLock {
            val oldPart = cache.get(key)?.toInternal()
            when {
                oldPart == null -> errorNotFound(id)
                oldPart.lock == PartLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldPart.lock != oldLock -> errorRepoConcurrency(oldPart, oldLock)
                else -> {
                    cache.invalidate(key)
                    DbPartResponseOk(oldPart)
                }
            }
        }
    }

    override suspend fun searchPart(rq: DbPartFilterRequest): IDbPartsResponse = tryPartsMethod {
        val result: List<Part> = cache.asMap().asSequence()
            .filter { entry ->
                rq.ownerId.takeIf { it != UserId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } ?: true
            }
            .filter { entry ->
                rq.nameFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.name?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        DbPartsResponseOk(result)
    }
}
