package models

import kotlin.jvm.JvmInline

@JvmInline
value class PartLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = PartLock("")
    }
}
