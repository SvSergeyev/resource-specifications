package repo

import models.Part
import models.PartError

sealed interface IDbPartsResponse: IDbResponse<List<Part>>

data class DbPartsResponseOk(
    val data: List<Part>
): IDbPartsResponse

@Suppress("unused")
data class DbPartsResponseErr(
    val errors: List<PartError> = emptyList()
): IDbPartsResponse {
    constructor(err: PartError): this(listOf(err))
}
