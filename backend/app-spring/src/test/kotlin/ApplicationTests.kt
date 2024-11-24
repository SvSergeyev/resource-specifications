package tech.sergeyev.education.app.spring

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import tech.sergeyev.education.app.spring.config.PartConfigPostgres

@SpringBootTest
class ApplicationTests {
    @Autowired
    var pgConf: PartConfigPostgres = PartConfigPostgres()


    @Test
    fun contextLoads() {
        Assertions.assertEquals(5433, pgConf.psql.port)
        Assertions.assertEquals("test_db", pgConf.psql.database)
    }
}
