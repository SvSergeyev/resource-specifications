import models.Part
import models.PartId
import models.PartLock
import tech.sergeyev.education.api.v1.models.PartCreateObject
import tech.sergeyev.education.api.v1.models.PartDeleteObject
import tech.sergeyev.education.api.v1.models.PartReadObject
import tech.sergeyev.education.api.v1.models.PartUpdateObject

fun Part.toTransportCreate() = PartCreateObject(
    name = name.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    materials = materials.takeIf { it.isNotEmpty() }?.mapKeys { it.key.name },
)

fun Part.toTransportRead() = PartReadObject(
    id = id.takeIf { it != PartId.NONE }?.asString(),
)

fun Part.toTransportUpdate() = PartUpdateObject(
    id = id.takeIf { it != PartId.NONE }?.asString(),
    name = name.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    materials = materials.takeIf { it.isNotEmpty() }?.mapKeys { it.key.name },
    lock = lock.takeIf { it != PartLock.NONE }?.asString(),
)

fun Part.toTransportDelete() = PartDeleteObject(
    id = id.takeIf { it != PartId.NONE }?.asString(),
    lock = lock.takeIf { it != PartLock.NONE }?.asString(),
)
