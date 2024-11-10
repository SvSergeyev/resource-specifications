package tech.sergeyev.education.e2e.be.test

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.collections.shouldExistInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import tech.sergeyev.education.api.v1.models.PartSearchFilter
import tech.sergeyev.education.api.v1.models.PartUpdateObject
import tech.sergeyev.education.e2e.be.test.action.v1.*

fun FunSpec.testApiV1(client: Client, prefix: String = "") {
    context("${prefix}v1") {
        test("Create Part ok") {
            client.createPart()
        }

        test("Read Part ok") {
            val created = client.createPart()
            client.readPart(created.id).asClue {
                it shouldBe created
            }
        }

        test("Update Part ok") {
            val created = client.createPart()
            val updatePart = PartUpdateObject(
                id = created.id,
                lock = created.lock,
                name = "Stopper",
                description = created.description,
                materials = created.materials,
            )
            client.updatePart(updatePart)
        }

        test("Delete Part ok") {
            val created = client.createPart()
            client.deletePart(created)
        }

        test("Search Part ok") {
            val created1 = client.createPart(someCreatePart.copy(name = "Sidewall"))
            val created2 = client.createPart(someCreatePart.copy(name = "Stopper"))

            withClue("Search S") {
                val results = client.searchPart(search = PartSearchFilter(searchString = "S"))
                results shouldHaveSize 2
                results shouldExist { it.name == created1.name }
                results shouldExist { it.name == created2.name }
            }

            withClue("Stop") {
                client.searchPart(search = PartSearchFilter(searchString = "Stop"))
                    .shouldExistInOrder({ it.name == created2.name })
            }
        }

        test("Report Part ok") {
            val part = client.createPart(someCreatePart)
            withClue("Create report for part") {
                val res = client.getPartReport(part.id)
                res.materials shouldBe part.materials
            }
        }
    }
}
