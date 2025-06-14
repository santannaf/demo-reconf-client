import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.graalvm.buildtools.native") version "0.10.6"
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.spring") version "2.1.20"
}

group = "org.example"
version = "0.0.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Library Reconf Client
    implementation("santannaf:reconf-client:0.0.1")
//    implementation("org.blocks4j.reconf:reconf-client:3.0.11") {
//        implementation("org.apache.commons:commons-collections4:4.4")
//        implementation("com.google.guava:guava:33.2.0-jre")
//        implementation("commons-io:commons-io:2.16.1")
//        implementation("org.apache.httpcomponents:httpclient:4.5.14")
//    }

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.bootJar {
    archiveFileName.set("app.jar")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register<Exec>("runCustomJar") {
    group = "application"
    description = "Run custom jar"
    dependsOn("bootJar")
    val appName = "app.jar"
    val addressesBuild = "./build/libs/$appName"
    commandLine(
        "java",
        "-agentlib:native-image-agent=config-merge-dir=./src/main/resources/META-INF/native-image/",
        "-jar",
        addressesBuild
    )
}

graalvmNative {
    binaries {
        named("main") {
            imageName.set("app")
            configurationFileDirectories.from(file("src/main/resources/META-INF/native-image"))
            mainClass.set("org.example.democlientreconf.ApplicationKt")
            verbose.set(true)
            debug.set(true)
            buildArgs("-J-Dfile.encoding=UTF-8")
            buildArgs("-J-Duser.country=BR")
            buildArgs("-march=native")
            buildArgs("--color=always")
            buildArgs("--allow-incomplete-classpath")
            buildArgs("--initialize-at-build-time=org.apache.commons.logging.LogFactoryService,org.apache.commons.logging.LogFactory")

        }
    }
}

configurations.all {
    exclude(group = "commons-logging", module = "commons-logging")
}

tasks.register("cleanExportDirs") {
    group = "build"
    description = "Remove all export folders from project and subprojects (including root level)"

    doLast {
        val exportDirs = mutableListOf<File>()

        // Verifica se tem export na raiz
        val rootExport = File(rootDir, "export")
        if (rootExport.exists() && rootExport.isDirectory) {
            exportDirs.add(rootExport)
        }

        // Procura export nos subdiretórios
        exportDirs.addAll(
            fileTree(rootDir) {
                include("**/export")
            }.files.filter { it.isDirectory }
        )

        exportDirs.forEach { dir ->
            println("Deleting export folder: ${dir.absolutePath}")
            dir.deleteRecursively()
        }
    }
}

tasks.named("clean") {
    finalizedBy("cleanExportDirs")
}
