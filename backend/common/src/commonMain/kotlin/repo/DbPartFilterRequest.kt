package repo

import models.UserId

data class DbPartFilterRequest(
    val nameFilter: String = "",
    val ownerId: UserId = UserId.NONE,
)
