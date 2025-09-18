package nl.rio282.simple_audio_cutter

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Simple Audio Cutter",
    ) {
        App()
    }
}