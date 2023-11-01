plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.parcelize")
    kotlin("plugin.serialization")
    id("com.google.gms.google-services")
}
android {
    namespace = "com.skelton.flowcache"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.skelton.flowcache"
        targetSdk = 34
        minSdk = 26
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    kotlinOptions {
        freeCompilerArgs += "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
        //  freeCompilerArgs += '-opt-in=kotlin.time.ExperimentalTime'
        freeCompilerArgs += "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
//        freeCompilerArgs += '-opt-in=kotlinx.coroutines.FlowPreview'
//        freeCompilerArgs += '-opt-in=androidx.compose.animation.ExperimentalAnimationApi'
//        useIR = true
//        allWarningsAsErrors = true
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val kotlinVersion = "1.8.10"
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.4.1")

    implementation("com.google.firebase:firebase-firestore-ktx:24.9.1")

    val lifecycle = "2.6.2"
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycle")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle")
//    implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.4.1"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-compose:1.8.0")

    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

//    implementation "androidx.compose.ui:ui:$compose_version"
//    implementation "androidx.compose.ui:ui-tooling:$compose_version"
//    implementation "androidx.compose.foundation:foundation:$compose_version"
//    implementation "androidx.compose.material:material:$compose_version"
//    implementation "androidx.compose.material:material-icons-core:$compose_version"
//    implementation "androidx.compose.material:material-icons-extended:$compose_version"

    val ktorVersion = "2.3.5"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-android:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")

    implementation ("com.google.accompanist:accompanist-swiperefresh:0.23.1")

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
//    implementation 'androidx.appcompat:appcompat:1.4.1'
//    implementation 'com.google.android.material:material:1.5.0'
//    implementation 'androidx.constraintlayout:constraintlayout:2.1.3'
}