buildscript {
	dependencies {
		classpath("org.postgresql:postgresql:42.7.1")
		classpath("org.flywaydb:flyway-database-postgresql:10.4.1")
	}
}

sourceSets {
	main {
		resources {
			srcDirs("src/main/resources")
			include("**/*.sql")

		}
	}
}

plugins {
	java
	id("org.springframework.boot") version "3.4.5"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.flywaydb.flyway") version "10.0.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}



repositories {
	mavenCentral()
}



dependencies {
	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")

	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql:10.4.1")

	implementation("org.postgresql:postgresql:42.7.3")

	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.amqp:spring-rabbit-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

flyway {
	url = "jdbc:postgresql://localhost:5432/postgres"
	user = "postgres"
	password = "postgres"
	locations = arrayOf("classpath:db/migration")
}


tasks.withType<Test> {
	useJUnitPlatform()
	testLogging {
		events("passed", "failed", "skipped", "standardOut", "standardError")
		showStandardStreams = true // 👈 ключевая настройка
	}
}

tasks.processResources {
	duplicatesStrategy = org.gradle.api.file.DuplicatesStrategy.EXCLUDE
}
