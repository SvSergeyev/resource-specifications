package models

import kotlin.jvm.JvmInline

@JvmInline
value class PartId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = PartId("")
    }
}
