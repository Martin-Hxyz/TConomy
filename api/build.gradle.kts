plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")
}

group = "xyz.handshot.tconomy.api"
version = "0.0.0"

repositories {
	mavenCentral()
}

dependencies {
	compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
	compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.2.2")
}