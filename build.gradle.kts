
version = "1.0-SNAPSHOT"
plugins {
    id("java")
    id("application")

    id("org.danilopianini.unibo-oop-gradle-plugin") version "1.0.13"}


repositories {
    mavenCentral()
}


tasks.jar {
    archiveBaseName.set("GrubClash")
    archiveVersion.set("1.0.0")
    archiveFileName.set("..\\..\\GrubClash.jar")

    manifest {
        attributes["Main-Class"] = "it.unibo.grubclash.model.Implementation.Main"
    }
}

dependencies {

    val jUnitVersion = "5.10.1"

    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")
}

application {
    mainClass.set("it.unibo.grubclash.model.Implementation.Main")
}
 java { toolchain { languageVersion.set(JavaLanguageVersion.of(17)) } }