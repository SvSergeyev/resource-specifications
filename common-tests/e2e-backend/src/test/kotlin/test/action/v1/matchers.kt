package tech.sergeyev.education.e2e.be.test.action.v1

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.and
import tech.sergeyev.education.api.v1.models.*


fun haveResult(result: ResponseResult) = Matcher<IResponse> {
    MatcherResult(
        it.result == result,
        { "actual result ${it.result} but we expected $result" },
        { "result should not be $result" }
    )
}

val haveNoErrors = Matcher<IResponse> {
    MatcherResult(
        it.errors.isNullOrEmpty(),
        { "actual errors ${it.errors} but we expected no errors" },
        { "errors should not be empty" }
    )
}

val haveSuccessResult = haveResult(ResponseResult.SUCCESS) and haveNoErrors

val IResponse.part: PartResponseObject?
    get() = when (this) {
        is PartCreateResponse -> part
        is PartReadResponse -> part
        is PartUpdateResponse -> part
        is PartDeleteResponse -> part
        is PartReportResponse -> part
        else -> throw IllegalArgumentException("Invalid response type: ${this::class}")
    }
