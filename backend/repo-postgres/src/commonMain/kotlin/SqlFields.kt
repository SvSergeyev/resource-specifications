object SqlFields {
    const val ID = "id"
    const val NAME = "name"
    const val DESCRIPTION = "description"
    const val MATERIAL = "material"
    const val QUANTITY = "quantity"
    const val LOCK = "lock"
    const val LOCK_OLD = "lock_old"
    const val OWNER_ID = "owner_id"
    const val PRODUCT_ID = "product_id"

    const val FILTER_NAME = NAME
    const val FILTER_OWNER_ID = OWNER_ID

    const val DELETE_OK = "DELETE_OK"

    fun String.quoted() = "\"$this\""
    val allFields = listOf(
        ID, NAME, DESCRIPTION, MATERIAL, QUANTITY, LOCK, OWNER_ID,
    )
}
