plugins {
  id("com.android.application")
  id("com.google.gms.google-services")
  kotlin("android")
  kotlin("kapt")
  kotlin("plugin.serialization") version LibVersion.kotlin
  id("kotlin-parcelize")
  id("androidx.navigation.safeargs.kotlin")
  id("dagger.hilt.android.plugin")
  id("com.google.firebase.crashlytics")
}

android {
  compileSdk = 32

  defaultConfig {
    applicationId = "com.himatifunpad.imazine"
    minSdk = 23
    targetSdk = 32
    versionCode = 5
    versionName = "3.0.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
    debug {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }
  compileOptions {
    isCoreLibraryDesugaringEnabled = true
    sourceCompatibility(JavaVersion.VERSION_11)
    targetCompatibility(JavaVersion.VERSION_11)
  }
  kotlinOptions {
    jvmTarget = "11"
    freeCompilerArgs = freeCompilerArgs + listOf(
      "-Xopt-in=kotlin.ExperimentalStdlibApi",
      "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
      "-Xopt-in=kotlin.RequiresOptIn"
    )
  }
  buildFeatures {
    viewBinding = true
  }
  packagingOptions {
    resources.excludes.add("/META-INF/AL2.0")
    resources.excludes.add("/META-INF/LGPL2.1")
  }
}

dependencies {
  implementation(kotlin("stdlib", version = LibVersion.kotlin))
  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${LibVersion.coroutines}")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${LibVersion.coroutines}")
  implementation(platform("com.google.firebase:firebase-bom:30.4.0"))

  // androidx core
  implementation("androidx.core:core-ktx:1.8.0")
  implementation("androidx.appcompat:appcompat:1.5.0")
  implementation("androidx.datastore:datastore-preferences:1.0.0")
  implementation("androidx.fragment:fragment-ktx:1.5.2")
  implementation("androidx.activity:activity-ktx:1.5.1")
  implementation("androidx.preference:preference-ktx:1.2.0")
  implementation("androidx.work:work-runtime-ktx:2.7.1")

  // ui
  implementation("com.google.android.material:material:1.6.1")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
  implementation("androidx.paging:paging-runtime-ktx:3.1.1")
  implementation("com.afollestad.material-dialogs:core:3.3.0")
  implementation("com.airbnb.android:lottie:5.2.0")

  // navigation
  implementation("androidx.navigation:navigation-fragment-ktx:${LibVersion.nav}")
  implementation("androidx.navigation:navigation-ui-ktx:${LibVersion.nav}")

  // lifecycle
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${LibVersion.lifecycle}")
  implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:${LibVersion.lifecycle}")
  implementation("androidx.lifecycle:lifecycle-livedata-ktx:${LibVersion.lifecycle}")

  // dagger hilt
  implementation("com.google.dagger:hilt-android:${LibVersion.hilt}")
  kapt("com.google.dagger:hilt-compiler:${LibVersion.hilt}")
  implementation("androidx.hilt:hilt-work:${LibVersion.androidxHilt}")
  kapt("androidx.hilt:hilt-compiler:${LibVersion.androidxHilt}")

  // networking
  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
  implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
  implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

  // firebase
  implementation("com.google.firebase:firebase-analytics-ktx")
  implementation("com.google.firebase:firebase-crashlytics-ktx")
  implementation("com.google.firebase:firebase-messaging-ktx")

  // other library
  implementation("io.coil-kt:coil:2.2.0")
  implementation("org.jsoup:jsoup:1.15.3")
  implementation("com.squareup.logcat:logcat:0.1")

  // testing
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.3")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

kapt {
  correctErrorTypes = true
}
