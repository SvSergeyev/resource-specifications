package tech.sergeyev.education.e2e.be.test.action.v1

import tech.sergeyev.education.api.v1.models.PartCreateObject
import tech.sergeyev.education.api.v1.models.PartDebug
import tech.sergeyev.education.api.v1.models.PartRequestDebugMode
import tech.sergeyev.education.api.v1.models.PartRequestDebugStubs

val debug = PartDebug(mode = PartRequestDebugMode.STUB, stub = PartRequestDebugStubs.SUCCESS)

val someCreatePart = PartCreateObject(
    name = "Stopper",
    description = "Just steel stopper",
    materials = mapOf("Лист металлический сталь 09Г2С 3,0 мм ГОСТ 19903-90" to 0.25)
)
