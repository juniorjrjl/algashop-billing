plugins {
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.spring.dependency.management)
	idea
	jacoco
	java
}

group = "com.algaworks.algashop"
version = "0.0.1-SNAPSHOT"
description = "billing microservice"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

val mockitoAgent = configurations.create("mockitoAgent")
configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {

	annotationProcessor(libs.hibernate.processor)
	annotationProcessor(libs.lombok)
	annotationProcessor(libs.lombok.mapstruct.binding)
	annotationProcessor(libs.mapstruct.processor)

	compileOnly(libs.lombok)

	implementation(libs.commons.lang3)
	implementation(libs.commons.validator)
	implementation(libs.mapstruct)
	implementation(libs.flyway.database.postgresql)
	implementation(libs.java.uuid.generator)
	implementation(libs.spring.boot.starter.data.jpa)
	implementation(libs.spring.boot.starter.flyway)
	implementation(libs.spring.boot.starter.restclient)
	implementation(libs.spring.boot.starter.validation)
	implementation(libs.spring.boot.starter.webmvc)

	mockitoAgent(libs.mockito.core) {
		isTransitive = false
	}

	runtimeOnly(libs.postgresql)

	testCompileOnly(libs.lombok)

	testAnnotationProcessor(libs.lombok)

	testImplementation(libs.assertj.core)
	testImplementation(libs.datafaker)
	testImplementation(libs.spring.boot.starter.data.jpa.test)
	testImplementation(libs.spring.boot.starter.flyway.test)
	testImplementation(libs.spring.boot.starter.webmvc.test)
	testImplementation(libs.wiremock.spring.boot)

	testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.withType<Test> {
	useJUnitPlatform()
	jvmArgs("-javaagent:${mockitoAgent.asPath}")
	finalizedBy(tasks.jacocoTestReport)
	systemProperty("test.seed", System.getProperty("test.seed") ?: "")
}

tasks.register<Test>("integrationTest"){
	description = "Run unit tests."
	group = "verification"

	jvmArgs("-javaagent:${mockitoAgent.asPath}")

	testClassesDirs = tasks.test.get().testClassesDirs
	classpath = tasks.test.get().classpath

	useJUnitPlatform{
		includeTags("IntegrationTest")
	}
	finalizedBy(tasks.jacocoTestReport)
	systemProperty("test.seed", System.getProperty("test.seed") ?: "")
}

tasks.register<Test>("unitTest"){
	description = "Run unit tests."
	group = "verification"

	jvmArgs("-javaagent:${mockitoAgent.asPath}")

	testClassesDirs = tasks.test.get().testClassesDirs
	classpath = tasks.test.get().classpath

	useJUnitPlatform{
		includeTags("UnitTest")
	}
	finalizedBy(tasks.jacocoTestReport)
	systemProperty("test.seed", System.getProperty("test.seed") ?: "")
}

tasks.jacocoTestReport {
	reports {
		xml.required = false
		csv.required = false
		html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
	}
}

tasks.bootJar {
	archiveFileName.set("billing.jar")
}

tasks.register<Exec>("dockerbuild"){
	description = "Builds a multi-platform Docker image using Buildx"
	group = "build"

	dependsOn("bootJar")

	workingDir = project.rootDir

	commandLine(
		"docker",
		"buildx",
		"build",
		"--platform",
		"linux/arm64/v8,linux/amd64",
		"--tag",
		"algashop/billing:dev",
		"."
	)
}
