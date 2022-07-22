import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("fabric-loom") version "0.12-SNAPSHOT"
	id("org.jetbrains.kotlin.jvm") version "1.7.10"
	id("org.jetbrains.kotlin.plugin.serialization") version "1.7.10"
	idea
}

val transitiveInclude: Configuration by configurations.creating

val archivesBaseName = property("archives_base_name")
group = property("maven_group")!!
version = property("mod_version")!!

base {
	archivesName.set(properties["archives_base_name"].toString())
}

repositories {
	mavenCentral()
	mavenLocal()
	maven("https://repo.repsy.io/mvn/amibeskyfy16/repo") // Use for my JsonConfig lib
}

dependencies {
	minecraft("com.mojang:minecraft:${properties["minecraft_version"]}")
	mappings("net.fabricmc:yarn:${properties["yarn_mappings"]}:v2")

	modImplementation("net.fabricmc:fabric-loader:${properties["loader_version"]}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${properties["fabric_version"]}")
	modImplementation("net.fabricmc:fabric-language-kotlin:${properties["fabric_kotlin_version"]}")

	transitiveInclude(implementation("ch.skyfy.jsonconfig:json-config:2.1.4")!!)


	handleIncludes(project, transitiveInclude)

	testImplementation("org.jetbrains.kotlin:kotlin-test:1.7.10")
}

tasks {

	val javaVersion = JavaVersion.VERSION_17

	processResources {
		inputs.property("version", project.version)
		filteringCharset = "UTF-8"
		filesMatching("fabric.mod.json") {
			expand(mutableMapOf("version" to project.version))
		}
	}

	java {
		withSourcesJar()
	}

	named<KotlinCompile>("compileKotlin") {
		kotlinOptions.jvmTarget = javaVersion.toString()
	}

	named<JavaCompile>("compileJava") {
		options.encoding = "UTF-8"
		options.release.set(javaVersion.toString().toInt())
	}

	named<Jar>("jar") {
		from("LICENSE") {
			rename { "${it}_${archivesBaseName}" }
		}
	}

	named<Test>("test") { // https://stackoverflow.com/questions/40954017/gradle-how-to-get-output-from-test-stderr-stdout-into-console
		useJUnitPlatform()

		testLogging {
			outputs.upToDateWhen { false } // When the build task is executed, stderr-stdout of test classes will be show
			showStandardStreams = true
		}
	}

//	val copyJarToTestServer = register("copyJarToTestServer") {
//		println("copy to server")
//		copyFile("build/libs/TinyEconomyRenewed-1.0.1.jar", project.property("testServerModsFolder") as String)
//	}
//
//	build {
//		doLast {
//			copyJarToTestServer.get()
//		}
//	}

}

fun DependencyHandlerScope.includeTransitive(
	root: ResolvedDependency?,
	dependencies: Set<ResolvedDependency>,
	fabricLanguageKotlinDependency: ResolvedDependency,
	checkedDependencies: MutableSet<ResolvedDependency> = HashSet()
) {
	dependencies.forEach {
		if (checkedDependencies.contains(it) || (it.moduleGroup == "org.jetbrains.kotlin" && it.moduleName.startsWith("kotlin-stdlib")) || (it.moduleGroup == "org.slf4j" && it.moduleName == "slf4j-api"))
			return@forEach

		if (fabricLanguageKotlinDependency.children.any { kotlinDep -> kotlinDep.name == it.name }) {
			println("Skipping -> ${it.name} (already in fabric-language-kotlin)")
		} else {
			include(it.name)
			println("Including -> ${it.name} from ${root?.name}")
		}
		checkedDependencies += it

		includeTransitive(root ?: it, it.children, fabricLanguageKotlinDependency, checkedDependencies)
	}
}

// from : https://github.com/StckOverflw/TwitchControlsMinecraft/blob/4bf406893544c3edf52371fa6e7a6cc7ae80dc05/build.gradle.kts
fun DependencyHandlerScope.handleIncludes(project: Project, configuration: Configuration) {
	includeTransitive(
		null,
		configuration.resolvedConfiguration.firstLevelModuleDependencies,
		project.configurations.getByName("modImplementation").resolvedConfiguration.firstLevelModuleDependencies
			.first { it.moduleGroup == "net.fabricmc" && it.moduleName == "fabric-language-kotlin" }
	)
}