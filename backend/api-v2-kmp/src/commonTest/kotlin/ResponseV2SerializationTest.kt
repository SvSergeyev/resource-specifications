import kotlinx.serialization.encodeToString
import models.Material
import tech.sergeyev.education.api.v2.models.IResponse
import tech.sergeyev.education.api.v2.models.PartCreateResponse
import tech.sergeyev.education.api.v2.models.PartResponseObject
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV2SerializationTest {
    private val response: IResponse = PartCreateResponse(
        part = PartResponseObject(
            name = "part name",
            description = "part description",
            materials = mapOf(Material.STEEL_PLATE_3.description to 0.1)
        )
    )

    @Test
    fun serialize() {
        val json = apiV2Mapper.encodeToString(response)

        println(json)

        assertContains(json, Regex("\"name\":\\s*\"part name\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(response)
        val obj = apiV2Mapper.decodeFromString<IResponse>(json) as PartCreateResponse

        assertEquals(response, obj)
    }
}
