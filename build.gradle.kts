plugins {
	idea
	java
	jacoco
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.spring.dependency.management)
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
	// IMPLEMENTATION
	implementation(libs.java.uuid.generator)
	implementation(libs.commons.validator)
	implementation(libs.commons.lang3)
	implementation(libs.mapstruct)
	implementation(libs.spring.boot.starter.data.jpa)
	implementation(libs.spring.boot.starter.webmvc)
	implementation(libs.spring.boot.starter.flyway)
	implementation(libs.spring.boot.starter.validation)
	implementation(libs.flyway.database.postgresql)

	// COMPILE ONLY & RUNTIME ONLY
	compileOnly(libs.lombok)
	runtimeOnly(libs.postgresql)

	// ANNOTATION PROCESSOR
	annotationProcessor(libs.mapstruct.processor)
	annotationProcessor(libs.lombok)
	annotationProcessor(libs.lombok.mapstruct.binding)
	annotationProcessor(libs.hibernate.processor)

	// TEST COMPILE & ANNOTATION PROCESSOR
	testCompileOnly(libs.lombok)
	testAnnotationProcessor(libs.lombok)

	// TEST IMPLEMENTATION
	testImplementation(libs.spring.boot.starter.flyway.test)
	testImplementation(libs.datafaker)
	testImplementation(libs.assertj.core)
	testImplementation(libs.spring.boot.starter.data.jpa.test)
	testImplementation(libs.spring.boot.starter.webmvc.test)

	// MOCKITO AGENT & RUNTIME
	mockitoAgent(libs.mockito.core) {
		isTransitive = false
	}
	testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.register<Test>("integrationTest"){
	description = "Run unit tests."
	group = "verification"

	testClassesDirs = tasks.test.get().testClassesDirs
	classpath = tasks.test.get().classpath

	useJUnitPlatform{
		includeTags("IntegrationTest")
	}
	systemProperty("test.seed", System.getProperty("test.seed") ?: "")
}

tasks.register<Test>("unitTest"){
	description = "Run unit tests."
	group = "verification"

	testClassesDirs = tasks.test.get().testClassesDirs
	classpath = tasks.test.get().classpath

	useJUnitPlatform{
		includeTags("UnitTest")
	}
	systemProperty("test.seed", System.getProperty("test.seed") ?: "")
}

tasks.jacocoTestReport {
	reports {
		xml.required = false
		csv.required = false
		html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
	}
}