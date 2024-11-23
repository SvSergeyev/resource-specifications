import models.*

data class PartEntity(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val ownerId: String? = null,
    val materials: Map<String, Double>? = null,
    val lock: String? = null,
) {
    constructor(model: Part): this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        name = model.name.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        ownerId = model.ownerId.asString().takeIf { it.isNotBlank() },
        materials = model.materials.map { (material, quantity) -> material.description to quantity }.toMap(),
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = Part(
            id = id?.let { PartId(it) }?: PartId.NONE,
            name = name?: "",
            description = description?: "",
            ownerId = ownerId?.let { UserId(it) }?: UserId.NONE,
            materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
            lock = lock?.let { PartLock(it) } ?: PartLock.NONE,
        )
}
