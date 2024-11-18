package controllers

import config.AppSettings
import controllerHelper
import fromTransport
import org.springframework.web.bind.annotation.*
import tech.sergeyev.education.api.v1.models.*
import toTransportPart
import kotlin.reflect.KClass

@Suppress("unused")
@RestController
@RequestMapping("v1/ad")
class AdControllerV1Fine(
    private val appSettings: AppSettings
) {

    @PostMapping("create")
    suspend fun create(@RequestBody request: PartCreateRequest): PartCreateResponse =
        process(appSettings, request = request, this::class, "create")

    @PostMapping("read")
    suspend fun read(@RequestBody request: PartReadRequest): PartReadResponse =
        process(appSettings, request = request, this::class, "read")

    @RequestMapping("update", method = [RequestMethod.POST])
    suspend fun update(@RequestBody request: PartUpdateRequest): PartUpdateResponse =
        process(appSettings, request = request, this::class, "update")

    @PostMapping("delete")
    suspend fun delete(@RequestBody request: PartDeleteRequest): PartDeleteResponse =
        process(appSettings, request = request, this::class, "delete")

    @PostMapping("search")
    suspend fun search(@RequestBody request: PartSearchRequest): PartSearchResponse =
        process(appSettings, request = request, this::class, "search")

    @PostMapping("report")
    suspend fun report(@RequestBody request: PartReportRequest): PartReportResponse =
        process(appSettings, request = request, this::class, "offers")

    companion object {
        suspend inline fun <reified Q : IRequest, reified R : IResponse> process(
            appSettings: AppSettings,
            request: Q,
            clazz: KClass<*>,
            logId: String,
        ): R = appSettings.controllerHelper(
            {
                fromTransport(request)
            },
            { toTransportPart() as R },
            clazz,
            logId,
        )
    }
}
