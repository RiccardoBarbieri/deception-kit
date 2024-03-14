import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import java.util.*

/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.3/userguide/building_java_projects.html in the Gradle documentation.
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    id("java")
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
	id("com.bmuschko.docker-spring-boot-application") version "9.3.2"
    id("application")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

group = "com.deceptionkit"
version = "1.0.1"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit test framework.

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.junit.platform:junit-platform-runner:1.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")


    // This dependency is used by the application.
    implementation("com.google.guava:guava:32.1.1-jre")

    // Keycloak
    implementation("org.keycloak:keycloak-admin-client:15.0.2")

	implementation("org.springframework.boot:spring-boot-starter-web:3.2.3")
    implementation("org.springframework.boot:spring-boot-starter:3.2.3")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.2.3")
    implementation("org.springframework.session:spring-session-core:3.2.1")
    implementation("org.springframework:spring-messaging:6.1.4")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("org.yaml:snakeyaml:2.2")

    implementation("log4j:log4j:1.2.17")
    implementation("org.slf4j:slf4j-api:2.0.9")


    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

//    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
//    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
//    implementation("org.springframework.boot:spring-boot-starter-security")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
//    testImplementation("org.springframework.boot:spring-boot-starter-test")
//    testImplementation("org.springframework.security:spring-security-test")

    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")

    implementation("com.fasterxml.jackson.core:jackson-core:2.16.0")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")


}


val springProps = Properties()

properties["activeProfile"]?.let {
    println("Loading properties from application-$it.properties")
    springProps.load(file("src/main/resources/application-$it.properties").inputStream())
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    systemProperty("spring.profiles.active", properties["activeProfile"] ?: "dev")
}

tasks.register<Copy>("propcopy") {
    dependsOn("processResources")
    group = "help"
    description = "Copy properties file to resources"
    val activeProfile = properties["activeProfile"] ?: "dev"
    from("src/main/resources/application-$activeProfile.properties")
    into("src/main/resources/")
    rename("application-$activeProfile.properties", "application.properties")
}


tasks.register<Dockerfile>("createDockerfile") {
    mustRunAfter("propcopy")
    mustRunAfter("bootDistTar")
    dependsOn("bootDistTar")
    dependsOn("propcopy")
    group = "unibobootdocker"
    description = "Create Dockerfile"

    doFirst {
        val inputTarFile =
            project.layout.projectDirectory.file("build/distributions/" + project.name + "-boot-" + project.version + ".tar")

        from("openjdk:17")
        exposePort(springProps["server.port"].toString().toInt())
        addFile("./build/distributions/${inputTarFile.asFile.name}", "/")
        workingDir(inputTarFile.asFile.name.removeSuffix(".tar") + "/bin")
        instruction("RUN mkdir resources")
        copyFile("./src/main/resources/*.json", "./resources/")
        defaultCommand("bash", "./" + project.name)
    }
}

tasks.register<DockerBuildImage>("buildImage") {
    dependsOn("createDockerfile")
    group = "unibobootdocker"
    description = "Dockerize the spring boot application"
    val dockerRepository = properties["dockerRepository"] ?: throw GradleException("dockerRepository property not set")
    dockerFile.set(file(layout.projectDirectory.toString() + "/build/docker/Dockerfile"))
    inputDir.set(file(layout.projectDirectory))
    images.add("${dockerRepository}/" + project.name.split(".").last().lowercase() + ":latest")
    images.add("${dockerRepository}/" + project.name.split(".").last().lowercase() + ":${project.version}")
}

tasks.register<DockerPushImage>("pushImage") {
    dependsOn("buildImage")
    group = "unibobootdocker"
    description = "Push the docker image to the repository"
    val dockerRepository = properties["dockerRepository"] ?: throw GradleException("dockerRepository property not set")
    images.add("${dockerRepository}/" + project.name.split(".").last().lowercase() + ":latest")
    images.add("${dockerRepository}/" + project.name.split(".").last().lowercase() + ":${project.version}")
}

application {
    // Define the main class for the application.
    mainClass.set("com.deceptionkit.DeceptionkitApplication")
}

tasks {
    test {
        testLogging.showExceptions = true
        useJUnitPlatform()
    }
}

//sourceSets {
//    test {
//        java {
//            srcDir("src/test/java")
//        }
//    }
//}