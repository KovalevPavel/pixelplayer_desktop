plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.audiotagger)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.koin.compose)

            implementation(project(":api_tags"))
        }
    }
}
