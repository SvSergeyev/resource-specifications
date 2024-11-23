package repo

import models.Part
import models.PartError

sealed interface IDbPartResponse: IDbResponse<Part>

data class DbPartResponseOk(
    val data: Part
): IDbPartResponse

data class DbPartResponseErr(
    val errors: List<PartError> = emptyList()
): IDbPartResponse {
    constructor(err: PartError): this(listOf(err))
}

data class DbPartResponseErrWithData(
    val data: Part,
    val errors: List<PartError> = emptyList()
): IDbPartResponse {
    constructor(part: Part, err: PartError): this(part, listOf(err))
}
