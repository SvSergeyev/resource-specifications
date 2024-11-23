package validation

import AppContext
import helpers.errorValidation
import helpers.fail
import models.State
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.chain
import tech.sergeyev.education.cor.worker

fun ICorChainDsl<AppContext>.validateSearchStringLength(title: String) = chain {
    this.title = title
    this.description = """
        Валидация длины строки поиска в поисковых фильтрах. Допустимые значения:
        - null - не выполняем поиск по строке
        - 3-100 - допустимая длина
        - больше 100 - слишком длинная строка
    """.trimIndent()
    on { state == State.RUNNING }
    worker("Обрезка пустых символов") { partFilterValidating.searchString = partFilterValidating.searchString.trim() }
    worker {
        this.title = "Проверка кейса длины на 0-2 символа"
        this.description = this.title
        on { state == State.RUNNING && partFilterValidating.searchString.length in (1..2) }
        handle {
            fail(
                errorValidation(
                    field = "searchString",
                    violationCode = "tooShort",
                    description = "Search string must contain at least 3 symbols"
                )
            )
        }
    }
    worker {
        this.title = "Проверка кейса длины на более 100 символов"
        this.description = this.title
        on { state == State.RUNNING && partFilterValidating.searchString.length > 100 }
        handle {
            fail(
                errorValidation(
                    field = "searchString",
                    violationCode = "tooLong",
                    description = "Search string must be no more than 100 symbols long"
                )
            )
        }
    }
}
