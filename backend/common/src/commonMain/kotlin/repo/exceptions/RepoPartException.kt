package repo.exceptions

import models.PartId

open class RepoPartException(
    @Suppress("unused")
    val partId: PartId,
    msg: String,
): RepoException(msg)
