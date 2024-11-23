package repo

import helpers.errorSystem

abstract class PartRepoBase: IRepoPart {

    protected suspend fun tryPartMethod(block: suspend () -> IDbPartResponse) = try {
        block()
    } catch (e: Throwable) {
        DbPartResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryPartsMethod(block: suspend () -> IDbPartsResponse) = try {
        block()
    } catch (e: Throwable) {
        DbPartsResponseErr(errorSystem("methodException", e = e))
    }

}
