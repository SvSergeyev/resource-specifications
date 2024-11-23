package mappers

import AppContext
import models.*
import stubs.Stubs
import tech.sergeyev.education.api.v2.models.*

fun AppContext.fromTransport(request: IRequest) = when (request) {
    is PartCreateRequest -> fromTransport(request)
    is PartReadRequest -> fromTransport(request)
    is PartUpdateRequest -> fromTransport(request)
    is PartDeleteRequest -> fromTransport(request)
    is PartSearchRequest -> fromTransport(request)
    is PartReportRequest -> fromTransport(request)
}

private fun String?.toPartId() = this?.let { PartId(it) } ?: PartId.NONE
private fun String?.toPartLock() = this?.let { PartLock(it) } ?: PartLock.NONE
private fun PartReadObject?.toInternal() = if (this != null) {
    Part(id = id.toPartId())
} else {
    Part()
}

private fun PartDebug?.transportToWorkMode(): WorkMode = when (this?.mode) {
    PartRequestDebugMode.PROD -> WorkMode.PROD
    PartRequestDebugMode.TEST -> WorkMode.TEST
    PartRequestDebugMode.STUB -> WorkMode.STUB
    null -> WorkMode.PROD
}

private fun PartDebug?.transportToStubCase(): Stubs = when (this?.stub) {
    PartRequestDebugStubs.SUCCESS -> Stubs.SUCCESS
    PartRequestDebugStubs.NOT_FOUND -> Stubs.NOT_FOUND
    PartRequestDebugStubs.BAD_ID -> Stubs.BAD_ID
    PartRequestDebugStubs.BAD_NAME -> Stubs.BAD_NAME
    PartRequestDebugStubs.BAD_DESCRIPTION -> Stubs.BAD_DESCRIPTION
    PartRequestDebugStubs.CANNOT_DELETE -> Stubs.CANNOT_DELETE
    PartRequestDebugStubs.BAD_SEARCH_STRING -> Stubs.BAD_SEARCH_STRING
    null -> Stubs.NONE
}

fun AppContext.fromTransport(request: PartCreateRequest) {
    command = Command.CREATE
    partRequest = request.part?.toInternal() ?: Part()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun AppContext.fromTransport(request: PartReadRequest) {
    command = Command.READ
    partRequest = request.part.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun AppContext.fromTransport(request: PartUpdateRequest) {
    command = Command.UPDATE
    partRequest = request.part?.toInternal() ?: Part()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun AppContext.fromTransport(request: PartDeleteRequest) {
    command = Command.DELETE
    partRequest = request.part.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun PartDeleteObject?.toInternal(): Part = if (this != null) {
    Part(
        id = id.toPartId(),
        lock = lock.toPartLock(),
    )
} else {
    Part()
}

fun AppContext.fromTransport(request: PartSearchRequest) {
    command = Command.SEARCH
    partFilterRequest = request.filter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun AppContext.fromTransport(request: PartReportRequest) {
    command = Command.REPORT
    partRequest = request.part.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun PartSearchFilter?.toInternal(): Filter = Filter(
    searchString = this?.searchString ?: "",
    ownerId = this?.ownerId?.let { UserId(it) } ?: UserId.NONE,
)

private fun PartCreateObject.toInternal(): Part = Part(
    name = this.name ?: "",
    description = this.description ?: "",
    materials = this.materials?.mapNotNull { (materialDescription, quantity) ->
        Material.fromDescription(materialDescription)?.let { material -> material to quantity }
    }?.toMap() ?: mutableMapOf(),
)

private fun PartUpdateObject.toInternal(): Part = Part(
    id = this.id.toPartId(),
    name = this.name ?: "",
    description = this.description ?: "",
    materials = this.materials?.mapNotNull { (materialDescription, quantity) ->
        Material.fromDescription(materialDescription)?.let { material -> material to quantity }
    }?.toMap() ?: mutableMapOf(),
    lock = lock.toPartLock(),
)
