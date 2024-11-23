package validation

import AppContext
import models.State
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker

fun ICorChainDsl<AppContext>.finishPartValidation(title: String) = worker {
    this.title = title
    on { state == State.RUNNING }
    handle {
        partValidated = partValidating
    }
}

fun ICorChainDsl<AppContext>.finishPartFilterValidation(title: String) = worker {
    this.title = title
    on { state == State.RUNNING }
    handle {
        partFilterValidated = partFilterValidating
    }
}
