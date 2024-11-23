import models.Part
import repo.IRepoPart

interface IRepoPartInitializable: IRepoPart {
    fun save(parts: Collection<Part>): Collection<Part>
}
