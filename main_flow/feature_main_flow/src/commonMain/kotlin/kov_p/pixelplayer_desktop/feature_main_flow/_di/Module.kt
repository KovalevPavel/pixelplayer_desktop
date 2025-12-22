package kov_p.pixelplayer_desktop.feature_main_flow._di

import androidx.compose.runtime.staticCompositionLocalOf
import org.koin.core.scope.Scope

val LocalMainScope = staticCompositionLocalOf<Scope> { error("No main flow scope found") }
