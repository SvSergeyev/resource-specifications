package tech.sergeyev.education.app.spring.mock

import apiV1RequestSerialize
import apiV1ResponseDeserialize
import models.Material
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import tech.sergeyev.education.api.v1.models.*
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Suppress("unused")
class PartControllerV1WsTest : PartControllerBaseWsTest<IRequest, IResponse>("v1") {

    @LocalServerPort
    override var port: Int = 0

    override fun deserializeRs(response: String): IResponse = apiV1ResponseDeserialize(response)
    override fun serializeRq(request: IRequest): String = apiV1RequestSerialize(request)

    @Test
    fun wsCreate(): Unit = testWsApp(
        PartCreateRequest(
            debug = PartDebug(PartRequestDebugMode.STUB, PartRequestDebugStubs.SUCCESS),
            part = PartCreateObject(
                name = "test1",
                description = "desc",
                materials = mapOf(Material.STEEL_PLATE_3.description to 0.1),
            )
        )
    ) { pl ->
        val mesInit = pl[0]
        val mesCreate = pl[1]
        assert(mesInit is PartInitResponse)
        assert(mesInit.result == ResponseResult.SUCCESS)
        assert(mesCreate is PartCreateResponse)
        assert(mesCreate.result == ResponseResult.SUCCESS)
    }
}