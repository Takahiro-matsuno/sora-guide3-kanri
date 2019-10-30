import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.2.0.RELEASE"
	id("io.spring.dependency-management") version "1.0.8.RELEASE"
	kotlin("jvm") version "1.3.50"
	kotlin("plugin.spring") version "1.3.50"
}

group = "jp.co.jalinfotec.soraguide"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	/**
	 * Spring Web MVC
	 */
	implementation("org.springframework.boot:spring-boot-starter-web")
	// thymeleaf
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:2.4.1")
	// webjars
	implementation("org.webjars:bootstrap:4.3.1")
	implementation("org.webjars:jquery:3.4.1")
	// test
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	/**
	 * Spring Data JPA
	 */
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	runtimeOnly("org.postgresql:postgresql")

	/**
	 * Spring Security
	 */
	implementation("org.springframework.boot:spring-boot-starter-security")
	// thymeleaf security拡張
	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity5:3.0.4.RELEASE")
	// test
	implementation("org.springframework.security:spring-security-test")

	// http://projectlombok.org/download.html
	implementation("org.projectlombok:lombok:1.16.6")

	/**
	 * Spring Retry
	 */
	implementation("org.springframework.retry:spring-retry")
	implementation("org.springframework.boot:spring-boot-starter-aop")

	/**
	 * SendGrid
	 */
	// https://mvnrepository.com/artifact/com.sendgrid/sendgrid-java
	implementation("com.sendgrid:sendgrid-java:4.4.1")


}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}