package repo

import models.Part
import models.PartId
import models.PartLock

data class DbPartIdRequest(
    val id: PartId,
    val lock: PartLock = PartLock.NONE,
) {
    constructor(part: Part): this(part.id, part.lock)
}
