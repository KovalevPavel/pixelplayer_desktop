plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            api(libs.ktor.client.core)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.negotiation)
            api(libs.ktor.client.json)

            implementation(libs.koin.compose)

            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}
