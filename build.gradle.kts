plugins {
	kotlin("jvm") version "1.5.20" apply (false)
	kotlin("plugin.serialization") version "1.5.20" apply (false)
}

group = "xyz.handshot.tconomy"
version = "0.0.0"

repositories {
	mavenCentral()
}

dependencies {
}

subprojects {
	tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
		kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
	}

	tasks.withType<Jar> {
		archiveBaseName.set("tconomy-${project.name}")
	}
}