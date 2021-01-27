plugins {
//    java
    kotlin("jvm") version "1.4.21"
}

group = "org.pedido.api"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    google()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.koin:koin-core:1.0.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.10.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.1")
    implementation("io.javalin:javalin:3.8.0")
    implementation("org.slf4j:slf4j-simple:1.7.28")
    implementation("com.h2database:h2:1.4.197")
    implementation("com.auth0:java-jwt:3.4.1")
    implementation("org.jetbrains.exposed:exposed:0.11.1")
    implementation("com.zaxxer:HikariCP:3.3.0")
    implementation("org.webjars:swagger-ui:3.24.3")
    implementation("io.swagger.core.v3:swagger-core:2.0.9")
    //testCompile("junit", "junit", "4.12")
}
