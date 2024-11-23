package models

data class Filter(
    var searchString: String = "",
    var ownerId: UserId = UserId.NONE,
) {
    fun deepCopy(): Filter = copy()

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = Filter()
    }
}
