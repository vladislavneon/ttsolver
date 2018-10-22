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
    flatDir {
        dir("libs")
    }
    maven { setUrl("https://kotlin.bintray.com/ktor") }
    maven { setUrl("https://kotlin.bintray.com/kotlinx") }
}

dependencies {
    compile("org.slf4j", "slf4j-log4j12", "1.7.25")
    compile("log4j", "log4j", "1.2.17")

    compile(files("libs/AspriseJavaPDF.jar"))
    compile("org.apache.pdfbox", "pdfbox", "2.0.12")

    compile("io.ktor", "ktor-server-netty", "0.9.5")
    compile("io.ktor", "ktor-jackson", "0.9.5")
    compile("io.ktor", "ktor-locations", "0.9.5")
    compile("org.jetbrains.kotlinx", "kotlinx-html-jvm", "0.6.11")

    compile(kotlin("stdlib-jdk8"))
}

kotlin {
    experimental.coroutines = Coroutines.ENABLE
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

idea {
    module {
        excludeDirs = setOf(file(".idea"), file("gradle"), file(".gradle"))
    }
}

task<Wrapper>("wrapper") {
    gradleVersion = "4.10"
}