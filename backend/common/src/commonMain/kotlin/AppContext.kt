import kotlinx.datetime.Instant
import models.*
import repo.IRepoPart
import stubs.Stubs
import models.PartError
import ws.IWsSession

data class AppContext(
    var command: Command = Command.NONE,
    var state: State = State.NONE,
    val errors: MutableList<PartError> = mutableListOf(),

    var corSettings: CorSettings = CorSettings(),
    var workMode: WorkMode = WorkMode.PROD,
    var stubCase: Stubs = Stubs.NONE,
    var wsSession: IWsSession = IWsSession.NONE,

    var requestId: RequestId = RequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var partRequest: Part = Part(),
    var partFilterRequest: Filter = Filter(),

    var partValidating: Part = Part(),
    var partFilterValidating: Filter = Filter(),

    var partValidated: Part = Part(),
    var partFilterValidated: Filter = Filter(),

    var partRepo: IRepoPart = IRepoPart.NONE,
    var partRepoRead: Part = Part(), // То, что прочитали из репозитория
    var partRepoPrepare: Part = Part(), // То, что готовим для сохранения в БД
    var partRepoDone: Part = Part(),  // Результат, полученный из БД
    var partsRepoDone: MutableList<Part> = mutableListOf(),

    var partResponse: Part = Part(),
    var partsResponse: MutableList<Part> = mutableListOf(),

    )
