import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.Year
import kotlin.math.max

plugins {
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
}

group = "net.asodev"
version = createVersion()

fun createVersion(): String {
    val env = System.getenv()
    return if (env["CI"] != "true") {
        "dev"
    } else {
        var runNumber = env["GITHUB_RUN_NUMBER"] ?: "0"
        runNumber = runNumber.padStart(max(3-runNumber.length, 0), '0')

        val year = Year.now().value
        "$year-$runNumber"
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks {
    jar {
        enabled = false
    }
}