package mappers

import AppContext
import UnknownCommand
import models.*
import tech.sergeyev.education.api.v1.models.PartError
import tech.sergeyev.education.api.v2.models.*

fun AppContext.toTransportPart(): IResponse = when (val cmd = command) {
    Command.CREATE -> toTransportCreate()
    Command.READ -> toTransportRead()
    Command.UPDATE -> toTransportUpdate()
    Command.DELETE -> toTransportDelete()
    Command.SEARCH -> toTransportSearch()
    Command.REPORT -> toTransportReport()
    Command.INIT -> toTransportInit()
    Command.FINISH -> throw UnknownCommand(cmd)
    Command.NONE -> throw UnknownCommand(cmd)
}

fun AppContext.toTransportCreate() = PartCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    part = partResponse.toTransportPart()
)

fun AppContext.toTransportRead() = PartReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    part = partResponse.toTransportPart()
)

fun AppContext.toTransportUpdate() = PartUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    part = partResponse.toTransportPart(),
)

fun AppContext.toTransportDelete() = PartDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    part = partResponse.toTransportPart(),

)

fun AppContext.toTransportSearch() = PartSearchResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    parts = partsResponse.toTransportPart()
)

fun AppContext.toTransportReport() = PartReportResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    materials = partResponse.getMaterialsUsage().map { (material, quantity) ->
        material.description to PartReportResponseAllOfMaterials(
            quantity = quantity,
            unit = material.unit
        )
    }.toMap()
)

fun AppContext.toTransportInit() = PartInitResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
)

fun List<Part>.toTransportPart(): List<PartResponseObject>? = this
    .map { it.toTransportPart() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun Part.toTransportPart(): PartResponseObject = PartResponseObject(
    id = id.takeIf { it != PartId.NONE }?.asString(),
    name = name.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != UserId.NONE }?.asString(),
    materials = materials.map { (material, quantity) -> material.description to quantity }.toMap(),
    lock = lock.takeIf { it != PartLock.NONE }?.asString()
)

private fun List<PartError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportPart() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun PartError.toTransportPart() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun State.toResult(): ResponseResult? = when (this) {
    State.RUNNING, State.FINISHING -> ResponseResult.SUCCESS
    State.FAILING -> ResponseResult.ERROR
    State.NONE -> null
}
