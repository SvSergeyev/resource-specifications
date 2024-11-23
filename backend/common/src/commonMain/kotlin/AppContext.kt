import kotlinx.datetime.Instant
import models.*
import stubs.Stubs
import tech.sergeyev.education.api.v1.models.PartError
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

    var partResponse: Part = Part(),
    var partsResponse: MutableList<Part> = mutableListOf(),

    )
