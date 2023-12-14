import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude
import org.jetbrains.dokka.DokkaDefaults.outputDir

plugins {
  id("com.github.ben-manes.versions") version "0.42.0"
  id("io.gitlab.arturbosch.detekt") version "1.21.0"
  id("org.jmailen.kotlinter") version "3.11.1"
}

buildscript {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:7.2.2")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${LibVersion.kotlin}")
    classpath("com.google.dagger:hilt-android-gradle-plugin:${LibVersion.hilt}")
    classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${LibVersion.nav}")
    // firebase
    classpath("com.google.gms:google-services:4.3.13")
    classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.1")
  }
}

allprojects {
  // apply ktlint
  apply(plugin = "org.jmailen.kotlinter")

  // Ktlint configuration for sub-projects
  kotlinter {
    ignoreFailures = false
    reporters = arrayOf("checkstyle", "plain")
    experimentalRules = false
    disabledRules = emptyArray()
  }
}

subprojects {
  // apply detekt
  apply(plugin = "io.gitlab.arturbosch.detekt")

  detekt {
    toolVersion = "1.21.0"
    config = files("${project.rootDir}/detekt.yml")
    buildUponDefaultConfig = true
  }
}

tasks.register("clean", Delete::class) {
  delete(rootProject.buildDir)
}

// ben-manes versions checking
fun isNonStable(version: String): Boolean {
  val stableKeyword = listOf("RELEASE", "FINAL", "GA").any {
    version.toUpperCase()
      .contains(it)
  }
  val regex = "^[0-9,.v-]+(-r)?$".toRegex()
  val isStable = stableKeyword || regex.matches(version)
  return isStable.not()
}

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
  // reject all non stable versions
  rejectVersionIf {
    isNonStable(candidate.version)
  }

  // optional parameters
  checkForGradleUpdate = true
  outputFormatter = "json"
  outputDir = "build/dependencyUpdates"
  reportfileName = "report"
}

// Kotlin DSL
tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
  reports {
    xml.required.set(false)
    html.required.set(true)
    txt.required.set(false)
    sarif.required.set(false)
    md.required.set(false)
  }
}
