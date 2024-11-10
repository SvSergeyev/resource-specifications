import org.junit.Test
import tech.sergeyev.education.api.v1.models.IResponse
import tech.sergeyev.education.api.v1.models.PartCreateResponse
import tech.sergeyev.education.api.v1.models.PartResponseObject
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV1SerializationTest {
    private val response = PartCreateResponse(
        part = PartResponseObject(
            name = "test name",
            description = "test description",
            materials = mapOf("test material id" to 1.0),
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        assertContains(json, Regex("\"name\":\\s*\"test name\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java)
        assertEquals(response, obj)
    }
}