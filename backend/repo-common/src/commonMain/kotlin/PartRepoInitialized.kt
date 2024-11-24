import models.Part

/**
 * Делегат для всех репозиториев, позволяющий инициализировать базу данных предзагруженными данными
 */
class PartRepoInitialized(
    val repo: IRepoPartInitializable,
    initObjects: Collection<Part> = emptyList(),
) : IRepoPartInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<Part> = save(initObjects).toList()
}
