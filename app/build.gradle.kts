plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("C:\\Users\\hp\\TunisiaLive\\mon_new_keystore.jks")
            storePassword = "fatouma.123"
            keyAlias = "mon_alias"
            keyPassword = "fatouma.123"
        }
    }
    namespace = "com.example.tunisialive"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tunisialive"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.work.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    dependencies {
        implementation(libs.firebase.auth)
        implementation(libs.credentials)
        implementation(libs.credentials.play.services.auth)
        implementation(libs.googleid)
        implementation(libs.firebase.functions)
        implementation(libs.firebase.messaging)
        implementation(libs.firebase.firestore)
        implementation(libs.firebase.auth)
        implementation(libs.credentials)
        implementation(libs.credentials.play.services.auth)
        implementation(libs.googleid)
        implementation(libs.firebase.database)



        dependencies {
            implementation("org.jsoup:jsoup:1.16.1")
            implementation("com.squareup.retrofit2:retrofit:2.9.0")
            implementation("com.squareup.retrofit2:converter-simplexml:2.9.0")
            implementation ("org.simpleframework:simple-xml:2.7.1")
            implementation("androidx.recyclerview:recyclerview:1.2.1")
            implementation("com.github.bumptech.glide:glide:4.12.0")
            implementation("com.github.bumptech.glide:compiler:4.12.0")
            implementation("com.google.firebase:firebase-firestore:24.7.0")
            implementation("com.google.firebase:firebase-auth:22.0.0")
            implementation("com.google.android.gms:play-services-auth:21.3.0")
            implementation("com.facebook.android:facebook-android-sdk:latest.release")

            // Room components
            implementation ("androidx.room:room-runtime:2.4.0")
            annotationProcessor ("androidx.room:room-compiler:2.4.0")
            implementation ("androidx.room:room-ktx:2.4.0")
            // Import the Firebase BoM (Bill of Materials)
            implementation(platform("com.google.firebase:firebase-bom:33.10.0"))

            // Firebase dependencies (no version needed when using BoM)
            implementation("com.google.firebase:firebase-analytics")
            implementation("com.google.firebase:firebase-firestore")
            implementation("com.google.firebase:firebase-auth")
            // Lifecycle components
            implementation ("androidx.lifecycle:lifecycle-viewmodel:2.4.0")
            implementation ("androidx.lifecycle:lifecycle-livedata:2.4.0")
            implementation ("androidx.lifecycle:lifecycle-common-java8:2.4.0")
            //les dépendances pour WorkManager
            implementation ("androidx.work:work-runtime:2.9.0")
        }}

    // Optional: If you need HTML text view
    // implementation("org.sufficientlysecure:html-textview:4.0")



//

}