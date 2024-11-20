import general.initStatus
import general.operation
import models.Command
import models.PartId
import models.PartLock
import stubs.*
import tech.sergeyev.education.cor.rootChain
import tech.sergeyev.education.cor.worker
import validation.*

const val stubsHandlingTitle = "Обработка стабов"
const val successTitle = "Имитация успешной обработки"
const val dbErrorTitle = "Имитация ошибки работы с БД"
const val validationErrorTitle = "Имитация ошибки валидации "
const val stubIsUnacceptable = "Ошибка: запрошенный стаб недопустим"
const val nonEmptyFieldCheckTitle = "Проверка на непустой "
const val idFormatCheckTitle = "Проверка формата id"
const val successValidationTitle = "Успешное завершение процедуры валидации"
const val copyToContextTitle = "Копируем поля в partValidating"
const val clearFieldTitle = "Очистка "
const val notEmptyFieldTitle = "Проверка, что %s не пусто: "
const val symbolsCheckTitle = "Проверка символов"

class PartProcessor(
    private val corSettings: CorSettings = CorSettings.NONE
) {
    suspend fun exec(ctx: AppContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain {
        initStatus("Инициализация статуса")

        operation("Создание", Command.CREATE) {
            stubs(stubsHandlingTitle) {
                stubCreateSuccess(successTitle, corSettings)
                stubValidationBadName(validationErrorTitle + "name")
                stubValidationBadDescription(validationErrorTitle + "description")
                stubDbError(dbErrorTitle)
                stubNoCase(stubIsUnacceptable)
            }
            validation {
                worker(copyToContextTitle) { partValidating = partRequest.deepCopy() }
                worker(clearFieldTitle + "id") { partValidating.id = PartId.NONE }
                worker(clearFieldTitle + "name") { partValidating.name = partValidating.name.trim() }
                worker(clearFieldTitle + "description") { partValidating.description = partValidating.description.trim() }
                validateNameNotEmpty(notEmptyFieldTitle + "name")
                validateNameHasContent(symbolsCheckTitle)
                validateDescriptionNotEmpty(notEmptyFieldTitle + "description")
                validateDescriptionHasContent(symbolsCheckTitle)
                finishPartValidation(successValidationTitle)
            }
        }
        operation("Получение", Command.READ) {
            stubs(stubsHandlingTitle) {
                stubReadSuccess(successTitle, corSettings)
                stubValidationBadId(validationErrorTitle + "id")
                stubDbError(dbErrorTitle)
                stubNoCase(stubIsUnacceptable)
            }
            validation {
                worker(copyToContextTitle) { partValidating = partRequest.deepCopy() }
                worker(clearFieldTitle + "id") { partValidating.id = PartId(partValidating.id.asString().trim()) }
                validateIdNotEmpty(nonEmptyFieldCheckTitle + "id")
                validateIdProperFormat(idFormatCheckTitle)
                finishPartValidation(successValidationTitle)
            }
        }
        operation("Изменение", Command.UPDATE) {
            stubs(stubsHandlingTitle) {
                stubUpdateSuccess(successTitle, corSettings)
                stubValidationBadId(validationErrorTitle + "id")
                stubValidationBadName(validationErrorTitle + "name")
                stubValidationBadDescription(validationErrorTitle + "description")
                stubDbError(dbErrorTitle)
                stubNoCase(stubIsUnacceptable)
            }
            validation {
                worker(copyToContextTitle) { partValidating = partRequest.deepCopy() }
                worker(clearFieldTitle + "id") { partValidating.id = PartId(partValidating.id.asString().trim()) }
                worker(clearFieldTitle + "lock") { partValidating.lock = PartLock(partValidating.lock.asString().trim()) }
                worker(clearFieldTitle + "name") { partValidating.name = partValidating.name.trim() }
                worker(clearFieldTitle + "description") { partValidating.description = partValidating.description.trim() }
                validateIdNotEmpty(nonEmptyFieldCheckTitle + "id")
                validateIdProperFormat(idFormatCheckTitle)
                validateLockNotEmpty(nonEmptyFieldCheckTitle + "lock")
                validateLockProperFormat("Проверка формата lock")
                validateNameNotEmpty(nonEmptyFieldCheckTitle + "name")
                validateNameHasContent("Проверка на наличие содержания в name")
                validateDescriptionNotEmpty(nonEmptyFieldCheckTitle + "description")
                validateDescriptionHasContent("Проверка на наличие содержания в описании")
                finishPartValidation(successValidationTitle)
            }
        }
        operation("Удаление", Command.DELETE) {
            stubs(stubsHandlingTitle) {
                stubDeleteSuccess(successTitle, corSettings)
                stubValidationBadId(validationErrorTitle + "id")
                stubDbError(dbErrorTitle)
                stubNoCase(stubIsUnacceptable)
            }
            validation {
                worker(copyToContextTitle) {
                    partValidating = partRequest.deepCopy()
                }
                worker(clearFieldTitle + "id") { partValidating.id = PartId(partValidating.id.asString().trim()) }
                worker(clearFieldTitle + "lock") { partValidating.lock = PartLock(partValidating.lock.asString().trim()) }
                validateIdNotEmpty(nonEmptyFieldCheckTitle + "id")
                validateIdProperFormat(idFormatCheckTitle)
                validateLockNotEmpty(nonEmptyFieldCheckTitle + "lock")
                validateLockProperFormat("Проверка формата lock")
                finishPartValidation(successValidationTitle)
            }
        }
        operation("Поиск", Command.SEARCH) {
            stubs(stubsHandlingTitle) {
                stubSearchSuccess(successTitle, corSettings)
                stubValidationBadId(validationErrorTitle + "id")
                stubDbError(dbErrorTitle)
                stubNoCase(stubIsUnacceptable)
            }
            validation {
                worker("Копируем поля в partFilterValidating") { partFilterValidating = partFilterRequest.deepCopy() }
                validateSearchStringLength("Валидация длины строки поиска в фильтре")
                finishPartFilterValidation(successValidationTitle)
            }
        }
        operation("Отчет", Command.REPORT) {
            stubs(stubsHandlingTitle) {
                stubReportSuccess(successTitle, corSettings)
                stubValidationBadId(validationErrorTitle + "id")
                stubDbError(dbErrorTitle)
                stubNoCase(stubIsUnacceptable)
            }
            validation {
                worker(copyToContextTitle) { partValidating = partRequest.deepCopy() }
                worker(clearFieldTitle + "id") { partValidating.id = PartId(partValidating.id.asString().trim()) }
                validateIdNotEmpty(nonEmptyFieldCheckTitle + "id")
                validateIdProperFormat(idFormatCheckTitle)
                finishPartValidation(successValidationTitle)
            }
        }
    }.build()
}
