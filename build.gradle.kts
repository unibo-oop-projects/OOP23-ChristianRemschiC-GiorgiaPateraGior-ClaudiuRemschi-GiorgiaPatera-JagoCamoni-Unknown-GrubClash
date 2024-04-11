group = "it.unibo.grubclash"
version = "1.0-SNAPSHOT"

plugins {
    id("java")
    id("application")
}

repositories {
    mavenCentral()
}

tasks.jar {
    archiveBaseName.set("GrubClash")
    archiveVersion.set("1.0.0")
    archiveFileName.set("..\\..\\GrubClash.jar")

    manifest {
        attributes["Main-Class"] = "it.unibo.grubclash.Main"
    }
}

application {
    mainClass.set("it.unibo.grubclash.Main")
}