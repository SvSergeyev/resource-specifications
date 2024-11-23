import com.benasher44.uuid.uuid4
import models.Part

object SqlTestCompanion {
    private const val HOST = "localhost"
    private const val USER = "postgres"
    private const val PASS = "postgres"
    val PORT = getEnv("postgresPort")?.toIntOrNull() ?: 5432

    fun repoUnderTestContainer(
        initObjects: Collection<Part> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() },
    ): IRepoPartInitializable = PartRepoInitialized(
        repo = RepoPartSql(
            SqlProperties(
                host = HOST,
                user = USER,
                password = PASS,
                port = PORT,
            ),
            randomUuid = randomUuid
        ),
        initObjects = initObjects,
    )
}

