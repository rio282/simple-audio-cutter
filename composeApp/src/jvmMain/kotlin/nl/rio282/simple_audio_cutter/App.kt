package nl.rio282.simple_audio_cutter

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import nl.rio282.simple_audio_cutter.view.MainView
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        MainView()
    }
}