data class SqlProperties(
    val host: String = "localhost",
    val port: Int = 5432,
    val user: String = "postgres",
    val password: String = "postgres",
    val database: String = "education_parts",
    val schema: String = "public",
    val table: String = "parts",
) {
    val url: String
        get() = "jdbc:postgresql://${host}:${port}/${database}"
}
