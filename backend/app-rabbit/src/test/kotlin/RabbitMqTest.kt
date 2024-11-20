package tech.sergeyev.education.app.rabbit

import apiV1Mapper
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.AfterClass
import org.junit.BeforeClass
import org.testcontainers.containers.RabbitMQContainer
import tech.sergeyev.education.api.v1.models.*
import tech.sergeyev.education.app.rabbit.config.AppSettings
import tech.sergeyev.education.app.rabbit.config.RabbitConfig
import tech.sergeyev.education.app.rabbit.config.RabbitExchangeConfiguration
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

//  тесты с использованием testcontainers
internal class RabbitMqTest {

    companion object {
        const val EXCHANGE = "test-exchange"
        const val EXCHANGE_TYPE = "direct"
        const val RMQ_PORT = 5672

        private val container = run {
//            Этот образ предназначен для дебагинга, он содержит панель управления на порту httpPort
            RabbitMQContainer("rabbitmq:3-management").apply {
//            Этот образ минимальный и не содержит панель управления
//            RabbitMQContainer("rabbitmq:latest").apply {
                withExposedPorts(5672, 15672) // Для 3-management
                withExposedPorts(RMQ_PORT)
            }
        }

        @BeforeClass
        @JvmStatic
        fun beforeAll() {
            container.start()
//            println("CONTAINER PORT (15672): ${container.getMappedPort(15672)}")
        }

        @AfterClass
        @JvmStatic
        fun afterAll() {
            container.stop()
        }
    }

    private val appSettings = AppSettings(
        rabbit = RabbitConfig(
            port = container.getMappedPort(RMQ_PORT)
        ),
        controllersConfigV1 = RabbitExchangeConfiguration(
            keyIn = "in-v1",
            keyOut = "out-v1",
            exchange = EXCHANGE,
            queue = "v1-queue",
            consumerTag = "v1-consumer-test",
            exchangeType = EXCHANGE_TYPE
        ),
    )
    private val app = RabbitApp(appSettings = appSettings)

    @BeforeTest
    fun tearUp() {
        app.start()
    }

    @AfterTest
    fun tearDown() {
        println("Test is being stopped")
        app.close()
    }

    @Test
    fun partCreateTestV1() {
        val (keyOut, keyIn) = with(appSettings.controllersConfigV1) { Pair(keyOut, keyIn) }
        val (tstHost, tstPort) = with(appSettings.rabbit) { Pair(host, port) }
        ConnectionFactory().apply {
            host = tstHost
            port = tstPort
            username = "guest"
            password = "guest"
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(EXCHANGE, "direct")
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, EXCHANGE, keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(EXCHANGE, keyIn, null, apiV1Mapper.writeValueAsBytes(boltCreateV1))

                runBlocking {
                    withTimeoutOrNull(1000L) {
                        while (responseJson.isBlank()) {
                            delay(10)
                        }
                    }
                }

                println("RESPONSE: $responseJson")
                val response = apiV1Mapper.readValue(responseJson, PartCreateResponse::class.java)
                val expected = PartStub.get()

                assertEquals(expected.name, response.part?.name)
                assertEquals(expected.description, response.part?.description)
            }
        }
    }

    private val boltCreateV1 = with(PartStub.get()) {
        PartCreateRequest(
            part = PartCreateObject(
                name = name,
                description = description
            ),
            requestType = "create",
            debug = PartDebug(
                mode = PartRequestDebugMode.STUB,
                stub = PartRequestDebugStubs.SUCCESS
            )
        )
    }
}
