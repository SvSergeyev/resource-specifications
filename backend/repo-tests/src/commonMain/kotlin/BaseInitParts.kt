import models.*

abstract class BaseInitParts(private val op: String): IInitObjects<Part> {
    open val lockOld: PartLock = PartLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: PartLock = PartLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        ownerId: UserId = UserId("owner-123"),
        lock: PartLock = lockOld,
    ) = Part(
        id = PartId("part-repo-$op-$suf"),
        name = "$suf stub",
        description = "$suf stub description",
        ownerId = ownerId,
        materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
        lock = lock,
    )
}
