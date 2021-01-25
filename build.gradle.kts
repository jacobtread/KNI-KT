plugins {
    kotlin("multiplatform") version "1.4.21"
}

group = "me.jacobtread"
version = "1.0.0"

repositories {
    jcenter()
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting
        val jvmMain by getting
    }
}
