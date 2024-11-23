import tech.sergeyev.education.logging.common.LoggerProvider
import ws.IWsSessionRepo

data class CorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
    val wsSessions: IWsSessionRepo = IWsSessionRepo.NONE,
) {
    companion object {
        val NONE = CorSettings()
    }
}
