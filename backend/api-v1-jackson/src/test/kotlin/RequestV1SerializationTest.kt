import org.junit.Test
import tech.sergeyev.education.api.v1.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {
    private val request = PartCreateRequest(
        debug = PartDebug(
            mode = PartRequestDebugMode.STUB,
            stub = PartRequestDebugStubs.BAD_NAME
        ),
        part = PartCreateObject(
            name = "test name",
            description = "test description",
            materials = mapOf("test material" to 1.0),
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        assertContains(json, Regex("\"name\":\\s*\"test name\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badName\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val jsonString = """
            {"part": null}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, PartCreateRequest::class.java)
        assertEquals(null, obj.part)
    }
}