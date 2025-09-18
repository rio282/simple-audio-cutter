package nl.rio282.simple_audio_cutter.component


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nl.rio282.simple_audio_cutter.model.AudioModel
import nl.rio282.simple_audio_cutter.util.formatMsTime

@Composable
fun AudioInfoComponent(audio: AudioModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("File: ${audio.file.name}")
        Text("Path: ${audio.file.absolutePath}")
        Text("Title: ${audio.title ?: "Unknown"}")
        Text("Artist: ${audio.artist ?: "Unknown"}")
        Text("Album: ${audio.album ?: "Unknown"}")
        Text("Duration: ${formatMsTime(audio.durationMs)} (${audio.durationMs}ms)")
    }
}