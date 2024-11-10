package tech.sergeyev.education.e2e.be.test

import docker.WiremockDockerCompose
import fixture.BaseFunSpec
import fixture.client.RestClient
import fixture.docker.DockerCompose
import io.kotest.core.annotation.Ignored

// Kotest не сможет подставить правильный аргумент конструктора, поэтому
// нужно запретить ему запускать этот класс
@Ignored
open class AccRestTestBase(dockerCompose: DockerCompose) : BaseFunSpec(dockerCompose, {
    val restClient = RestClient(dockerCompose)
    testApiV1(restClient, "rest ")
})

class AccRestWiremockTest : AccRestTestBase(WiremockDockerCompose)
