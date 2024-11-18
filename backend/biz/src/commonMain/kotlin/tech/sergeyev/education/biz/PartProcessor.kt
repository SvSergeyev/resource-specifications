import models.State

@Suppress("unused", "RedundantSuspendModifier")
class PartProcessor(val corSettings: CorSettings) {
    suspend fun exec(ctx: AppContext) {
        ctx.partResponse = PartStub.get()
        ctx.partsResponse = PartStub.prepareSearchList("plate").toMutableList()
        ctx.state = State.RUNNING
    }
}