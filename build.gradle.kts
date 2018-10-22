import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "org.csc"
version = "1.0-SNAPSHOT"

plugins {
    idea apply true
    kotlin("jvm") version "1.2.71"
}

repositories {
    jcenter()
    maven { setUrl("https://kotlin.bintray.com/ktor") }
    maven { setUrl("https://kotlin.bintray.com/kotlinx") }
}

dependencies {
    compile("io.ktor", "ktor-server-netty", "0.9.2")
    compile("io.ktor", "ktor-jackson", "0.9.2")
    compile("io.ktor", "ktor-locations", "0.9.2")
    compile("org.jetbrains.kotlinx", "kotlinx-html-jvm", "0.6.11")
    compile("org.primefaces", "primefaces", "6.0")


    compile(kotlin("stdlib-jdk8"))
}

kotlin {
    experimental.coroutines = Coroutines.ENABLE
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

idea  {
    module {
        excludeDirs = setOf(file(".idea"), file("gradle"))
    }
}

task<Wrapper>("wrapper") {
    gradleVersion = "4.10"
}