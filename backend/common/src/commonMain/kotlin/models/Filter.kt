package models

data class Filter(
    var searchString: String = "",
    var ownerId: UserId = UserId.NONE,
)
