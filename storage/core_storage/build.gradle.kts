plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.datastore.core)
            implementation(libs.datastore.preferences)

            implementation(libs.koin.compose)

            implementation(libs.kotlinx.coroutinesSwing)

            implementation(project(":api_storage"))
        }
    }
}
