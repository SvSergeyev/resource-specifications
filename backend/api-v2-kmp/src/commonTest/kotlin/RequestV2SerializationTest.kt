import kotlinx.serialization.encodeToString
import models.Material
import tech.sergeyev.education.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV2SerializationTest {
    private val request: IRequest = PartCreateRequest(
        debug = PartDebug(
            mode = PartRequestDebugMode.STUB,
            stub = PartRequestDebugStubs.BAD_NAME
        ),
        part = PartCreateObject(
            name = "part name",
            description = "part description",
            materials = mapOf(Material.STEEL_PLATE_3.description to 0.1),
        )
    )

    @Test
    fun serialize() {
        val json = apiV2Mapper.encodeToString(IRequest.serializer(), request)

        println(json)

        assertContains(json, Regex("\"name\":\\s*\"part name\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badName\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(request)
        val obj = apiV2Mapper.decodeFromString<IRequest>(json) as PartCreateRequest

        assertEquals(request, obj)
    }
    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"part": null}
        """.trimIndent()
        val obj = apiV2Mapper.decodeFromString<PartCreateRequest>(jsonString)

        assertEquals(null, obj.part)
    }
}
