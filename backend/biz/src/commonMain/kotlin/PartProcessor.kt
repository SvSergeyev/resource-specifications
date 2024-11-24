import general.initStatus
import general.operation
import models.Command
import models.PartId
import models.PartLock
import models.State
import repo.*
import stubs.*
import tech.sergeyev.education.cor.chain
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
        initRepo("Инициализация репозитория")


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
            chain {
                title = "Логика сохранения"
                repoPrepareCreate("Подготовка объекта для сохранения")
                repoCreate("Создание в БД")
            }
            prepareResult("Подготовка ответа")

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
            chain {
                title = "Логика чтения"
                repoRead("Чтение из БД")
                worker {
                    title = "Подготовка ответа для Read"
                    on { state == State.RUNNING }
                    handle { partRepoDone = partRepoRead }
                }
            }
            prepareResult("Подготовка ответа")

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
            chain {
                title = "Логика сохранения"
                repoRead("Чтение из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareUpdate("Подготовка объекта для обновления")
                repoUpdate("Обновление в БД")
            }
            prepareResult("Подготовка ответа")

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
            chain {
                title = "Логика удаления"
                repoRead("Чтение из БД")
                repoPrepareDelete("Подготовка объекта для удаления")
                repoDelete("Удаление из БД")
            }
            prepareResult("Подготовка ответа")

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
            repoSearch("Поиск в БД по фильтру")
            prepareResult("Подготовка ответа")
        }
    }.build()
}
