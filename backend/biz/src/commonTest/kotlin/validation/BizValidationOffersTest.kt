package validation

import models.Command
import kotlin.test.Test

class BizValidationOffersTest: BaseBizValidationTest() {
    override val command = Command.REPORT

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

}
