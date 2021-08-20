plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("com.github.johnrengelman.shadow") version "7.0.0"
	id("io.github.slimjar") version "1.2.2"
}

group = "xyz.handshot.tconomy"
version = "0.0.0"

repositories {
	mavenCentral()
	maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
	maven("https://jitpack.io")
	maven("https://repo.vshnv.tech/releases")
	maven("https://hub.spigotmc.org/nexus/content/groups/public/")
	maven("https://oss.sonatype.org/content/groups/public/")
}

dependencies {
	compileOnly("org.spigotmc:spigot-api:1.17-R0.1-SNAPSHOT")
	compileOnly("com.github.MilkBowl:VaultAPI:1.7")
	implementation(project(":api"))
	implementation("io.github.slimjar:slimjar:1.2.5")
	slim("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
	slim("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.2.2")
}

tasks.withType<Jar> {
	configurations["compileClasspath"].forEach { file: File ->
		if (file.name.startsWith("slimjar") || file.name.startsWith("tconomy-api"))
		{
			from(zipTree(file.absoluteFile))
		}
	}
}