import kotlinx.coroutines.test.runTest
import mappers.fromTransport
import mappers.toTransportPart
import models.Material
import tech.sergeyev.education.api.v1.models.*
import tech.sergeyev.education.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerV2Test {

    private val request = PartCreateRequest(
        part = PartCreateObject(
            name = "some part",
            description = "some description of some part",
            materials = mapOf(Material.STEEL_PLATE_3.description to 0.1),
        ),
        debug = PartDebug(
            mode = PartRequestDebugMode.STUB,
            stub = PartRequestDebugStubs.SUCCESS
        )
    )

    private val appSettings: IAppSettings = object : IAppSettings {
        override val corSettings: CorSettings = CorSettings()
        override val processor: PartProcessor = PartProcessor(corSettings)
    }

    private suspend fun createAdSpring(request: PartCreateRequest): PartCreateResponse =
        appSettings.controllerHelper(
            { fromTransport(request) },
            { toTransportPart() as PartCreateResponse },
            ControllerV2Test::class,
            "controller-v2-test"
        )

    class TestApplicationCall(private val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(res: IResponse) {
            this.res = res
        }
    }

    private suspend fun TestApplicationCall.createAdKtor(appSettings: IAppSettings) {
        val resp = appSettings.controllerHelper(
            { fromTransport(receive<PartCreateRequest>()) },
            { toTransportPart() },
            ControllerV2Test::class,
            "controller-v2-test"
        )
        respond(resp)
    }

    @Test
    fun springHelperTest() = runTest {
        val res = createAdSpring(request)
        assertEquals(ResponseResult.SUCCESS, res.result)
    }

    @Test
    fun ktorHelperTest() = runTest {
        val testApp = TestApplicationCall(request).apply { createAdKtor(appSettings) }
        val res = testApp.res as PartCreateResponse
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}
