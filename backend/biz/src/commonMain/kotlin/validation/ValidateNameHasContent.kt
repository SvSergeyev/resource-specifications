package validation

import AppContext
import helpers.errorValidation
import helpers.fail
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker

fun ICorChainDsl<AppContext>.validateNameHasContent(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем, что у нас есть какие-то слова в name.
        Отказываем в create name, в которых только бессмысленные символы типа %^&^$^%#^))&^*&%^^&
    """.trimIndent()
    val regExp = Regex("\\p{L}")
    on { partValidating.name.isNotEmpty() && ! partValidating.name.contains(regExp) }
    handle {
        fail(
            errorValidation(
            field = "name",
            violationCode = "noContent",
            description = "field must contain letters"
        )
        )
    }
}
