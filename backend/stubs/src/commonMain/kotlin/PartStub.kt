import PartStubPlates.STUB_PLATE
import models.Material
import models.Part
import models.PartId

object PartStub {
    fun get(): Part = STUB_PLATE.copy()

    fun prepareResult(block: Part.() -> Unit): Part = get().apply(block)

    fun prepareSearchList(filter: String) = listOf(
        searchPart("d-666-01", filter),
        searchPart("d-666-02", filter),
        searchPart("d-666-03", filter),
        searchPart("d-666-04", filter),
        searchPart("d-666-05", filter),
        searchPart("d-666-06", filter),
    )

    fun prepareReport() = report(STUB_PLATE)

    private fun searchPart(id: String, filter: String) =
        part(STUB_PLATE, id = id, filter = filter)

    private fun part(base: Part, id: String, filter: String) = base.copy(
        id = PartId(id),
        name = "$filter $id",
        description = "desc $filter $id",
        materials = mapOf(Material.STEEL_PLATE_3 to 0.1),
    )

    private fun report(base: Part) = base.copy(
        materials = base.getMaterialsUsage(),
    )

}