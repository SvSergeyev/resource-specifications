import models.Material
import models.Part
import models.PartId
import models.UserId

object PartStubPlates {
    val STUB_PLATE: Part
        get() = Part(
            id = PartId("666"),
            name = "plate",
            description = "so sad",
            ownerId = UserId("user-1"),
            materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
        )
}