import repo.IRepoPart
import tech.sergeyev.education.logging.common.LoggerProvider
import ws.IWsSessionRepo

data class CorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
    val wsSessions: IWsSessionRepo = IWsSessionRepo.NONE,
    val repoStub: IRepoPart = IRepoPart.NONE,
    val repoTest: IRepoPart = IRepoPart.NONE,
    val repoProd: IRepoPart = IRepoPart.NONE,

    ) {
    companion object {
        val NONE = CorSettings()
    }
}
