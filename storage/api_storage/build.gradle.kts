plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.compose)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}
