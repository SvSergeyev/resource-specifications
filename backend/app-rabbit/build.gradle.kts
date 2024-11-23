plugins {
    id("build-jvm")
    application
    alias(libs.plugins.shadowJar)
    alias(libs.plugins.muschko.java)
}

application {
    mainClass.set("tech.sergeyev.education.app.rabbit.ApplicationKt")
}

dependencies {

    implementation(kotlin("stdlib"))

    implementation(libs.rabbitmq.client)
    implementation(libs.jackson.databind)
    implementation(libs.logback)
    implementation(libs.coroutines.core)

    implementation(project(":common"))
    implementation(project(":app-common"))
    implementation("tech.sergeyev.education.libs:lib-logging-logback")

    // v1 api
    implementation(project(":api-v1-jackson"))
    implementation(project(":api-v1-mappers"))

    // v2 api
    implementation(project(":api-v2-kmp"))

    implementation(project(":biz"))
    implementation(project(":stubs"))

    testImplementation(libs.testcontainers.rabbitmq)
    testImplementation(kotlin("test"))
}