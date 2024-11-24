plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("androidx.navigation.safeargs.kotlin") // safe args
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") // manage api keys
    id("kotlin-parcelize") // parcelize
    id("com.google.devtools.ksp") // ksp
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android") // hilt
    id("com.google.firebase.crashlytics") // firebase crashlytics
    id("com.google.firebase.firebase-perf") // firebase performance
    id("com.google.gms.google-services") // google services
}

android {
    namespace = "com.okebenoithub.android.www"
    compileSdk = 34
    buildToolsVersion = "34.0.0"
    // resolve conflicts between the support library and the AndroidX libraries
    // useLibrary("org.apache.http.legacy")

    buildFeatures {
        dataBinding = true // enable data binding
        viewBinding = true // enable view binding
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.okebenoithub.android.www"
        minSdk = 24
        targetSdk = 34
        versionCode = 4
        versionName = "4.0.0"
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
        buildConfigField("String","YOUTUBE_API_KEY","\"YOUTUBE_API_KEY\"")
        buildConfigField("String","YOUTUBE_BASE_URL","\"YOUTUBE_BASE_URL\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    // Allow references to generated code
    kapt {
        correctErrorTypes = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
        // for view model factory dependencies
        freeCompilerArgs += ("-Xjvm-default=all")
    }
}

dependencies {
    // core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // material ui
    implementation(libs.material)
    implementation(libs.androidx.activity)

    // constraint layout
    implementation(libs.androidx.constraintlayout)

    // databinding
    implementation(libs.androidx.databinding.runtime)
    implementation(libs.androidx.legacy.support.v4)

    // view model and livedata
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // lifecycle runtime
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // fragment navigation ui
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.fragment.ktx)

    // glide
    implementation(libs.glide)
    implementation(libs.interactivemedia)
    ksp(libs.compiler)

    // room database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // date time utils
    implementation(libs.datetimeutils)

    // material ripple
    implementation(libs.material.ripple)

    // coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // work manager
    implementation(libs.androidx.work.runtime.ktx)

    // dexter runtime permissions
    implementation(libs.dexter)

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // logging interceptor
    implementation(libs.logging.interceptor)

    // color palette api
    implementation(libs.androidx.palette)

    // circle image view
    implementation(libs.circleimageview)

    // hilt dependency injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // youtube player API
    implementation(libs.ytPlayerCore)

    // pick image and video from gallery
    implementation(libs.imagepicker)

    // photo editor
    implementation(libs.photoeditor)

    // pdf viewer
    implementation(libs.pdf.viewer)

    // data store for for storing simple key-value pairs
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // object store for storing complex data structures
    implementation("com.orhanobut:hawk:2.0.1")

    // exoplayer media 3
    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation("androidx.media3:media3-exoplayer-dash:1.3.1")
    implementation("androidx.media3:media3-ui:1.3.1")

    // cameraX
    implementation("androidx.camera:camera-core:1.3.4")
    implementation("androidx.camera:camera-camera2:1.3.4")
    implementation("androidx.camera:camera-lifecycle:1.3.4")
    implementation("androidx.camera:camera-video:1.3.4")
    implementation ("androidx.camera:camera-view:1.3.4")
    implementation("androidx.camera:camera-extensions:1.3.4")

    // okhttp
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    implementation("com.github.varungulatii:Kdownloader:1.0.4")

    //////////////////////////////// Firebase //////////////////////////////
    implementation(platform(libs.firebase.bom)) // firebase bom

    // Add the dependency for the Firebase SDK for Google Analytics
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation(libs.firebase.analytics.ktx) // firebase analytics

    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries

    implementation(libs.firebase.auth.ktx) // firebase auth
    implementation(libs.firebase.firestore.ktx) // firebase firestore
    implementation(libs.firebase.messaging.ktx) // firebase messaging
    implementation(libs.firebase.storage.ktx) // firebase storage
    implementation(libs.firebase.dynamic.links.ktx) // firebase dynamic links
    implementation(libs.firebase.crashlytics.ktx) // firebase crashlytics
    implementation(libs.firebase.perf.ktx) // firebase performance

    /////////////////////////////// Testing ///////////////////////////////
    // room testing
    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}