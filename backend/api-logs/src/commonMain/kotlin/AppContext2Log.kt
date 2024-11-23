import kotlinx.datetime.Clock
import models.*
import tech.sergeyev.education.api.logs.models.*
import models.PartError

fun AppContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "education",
    part = toAppLog(),
    errors = errors.map { it.toLog() },
)

private fun AppContext.toAppLog(): PartLogModel? {
    val partNone = Part()
    return PartLogModel(
        requestId = requestId.takeIf { it != RequestId.NONE }?.asString(),
        requestPart = partRequest.takeIf { it != partNone }?.toLog(),
        responsePart = partResponse.takeIf { it != partNone }?.toLog(),
        responseParts = partsResponse.takeIf { it.isNotEmpty() }?.filter { it != partNone }?.map { it.toLog() },
        requestFilter = partFilterRequest.takeIf { it != Filter() }?.toLog(),
    ).takeIf { it != PartLogModel() }
}

private fun Filter.toLog() = PartFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != UserId.NONE }?.asString(),
)

private fun PartError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
)

private fun Part.toLog() = PartLog(
    id = id.takeIf { it != PartId.NONE }?.asString(),
    name = name.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    materials = materials.takeIf { it.isNotEmpty() }?.mapKeys { it.key.name }
)
