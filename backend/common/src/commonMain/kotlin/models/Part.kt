package models

data class Part(
    var id: PartId = PartId.NONE,
    var name: String = "",
    var description: String = "",
    var ownerId: UserId = UserId.NONE,
    var materials: Map<Material, Double> = mapOf(),
    var lock: PartLock = PartLock.NONE,
) {
    fun getMaterialsUsage(): Map<Material, Double> {
        val result = mutableMapOf<Material, Double>()
        materials.forEach { (material, quantity) ->
            val currentQuantity = result[material] ?: 0.0
            result[material] = currentQuantity + quantity
        }
        return result
    }

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = Part()
    }

}
