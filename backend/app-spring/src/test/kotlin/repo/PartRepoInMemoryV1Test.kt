package repo

import PartRepoInMemory
import PartRepoInitialized
import PartStub
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.slot
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import tech.sergeyev.education.app.spring.config.PartConfig
import tech.sergeyev.education.app.spring.controllers.PartControllerV1Fine
import tech.sergeyev.education.app.spring.repo.RepoInMemoryConfig
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(
    PartControllerV1Fine::class, PartConfig::class,
    properties = ["spring.main.allow-bean-definition-overriding=true"]
)
@Import(RepoInMemoryConfig::class)internal class PartRepoInMemoryV1Test : PartRepoBaseV1Test() {
    @Autowired
    override lateinit var webClient: WebTestClient

    @MockkBean
    @Qualifier("testRepo")
    lateinit var testTestRepo: IRepoPart

    @BeforeEach
    fun tearUp() {
        val slotPart = slot<DbPartRequest>()
        val slotId = slot<DbPartIdRequest>()
        val slotFl = slot<DbPartFilterRequest>()
        val repo = PartRepoInitialized(
            repo = PartRepoInMemory(randomUuid = { uuidNew }),
            initObjects = PartStub.prepareSearchList("xx") + PartStub.get()
        )
        coEvery { testTestRepo.createPart(capture(slotPart)) } coAnswers { repo.createPart(slotPart.captured) }
        coEvery { testTestRepo.readPart(capture(slotId)) } coAnswers { repo.readPart(slotId.captured) }
        coEvery { testTestRepo.updatePart(capture(slotPart)) } coAnswers { repo.updatePart(slotPart.captured) }
        coEvery { testTestRepo.deletePart(capture(slotId)) } coAnswers { repo.deletePart(slotId.captured) }
        coEvery { testTestRepo.searchPart(capture(slotFl)) } coAnswers { repo.searchPart(slotFl.captured) }
    }

    @Test
    override fun createPart() = super.createPart()

    @Test
    override fun readPart() = super.readPart()

    @Test
    override fun updatePart() = super.updatePart()

    @Test
    override fun deletePart() = super.deletePart()

    @Test
    override fun searchPart() = super.searchPart()

    @Test
    override fun reportPart() = super.reportPart()
}
