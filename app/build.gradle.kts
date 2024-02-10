import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

android {
    namespace = "com.example.pintube"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pintube"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "YOUTUBE_API_KEY", properties.getProperty("YOUTUBE_API_KEY"))
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    dependencies {

        implementation("androidx.core:core-ktx:1.12.0")
        implementation("androidx.appcompat:appcompat:1.6.1")
        implementation("com.google.android.material:material:1.11.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
        implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
        implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
        implementation ("com.github.bumptech.glide:glide:4.12.0")

        //by viewModels를 사용하기 위한 의존성
        implementation("androidx.activity:activity-ktx:1.8.2")
        implementation("androidx.fragment:fragment-ktx:1.6.2")

        //retrofit
        implementation("com.google.code.gson:gson:2.10.1")
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation("com.squareup.okhttp3:okhttp:4.10.0")
        implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

        // coil
        implementation("io.coil-kt:coil:2.3.0")

        implementation("androidx.room:room-runtime:2.6.1") // Room 라이브러리
        ksp("androidx.room:room-compiler:2.6.1") // Room의 애노테이션 프로세서

        //hilt
        implementation("com.google.dagger:hilt-android:2.50")
        ksp("com.google.dagger:hilt-android-compiler:2.50")

        // 더보기
        implementation("kr.co.prnd:readmore-textview:1.0.0")

        // youtube player
        implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")

        // jsoup html데이터 파싱
        implementation("org.jsoup:jsoup:1.17.2")
    }
}
