package nl.rio282.simple_audio_cutter.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

@Composable
fun PlayPauseButton(
    inPlayback: Boolean,
    onToggle: () -> Unit
) {
    Button(
        onClick = onToggle,
        modifier = Modifier.height(32.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (inPlayback) Color.Red else Color.Green,
            contentColor = Color.White
        )
    ) {
        Icon(
            imageVector = if (inPlayback) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = if (inPlayback) "Pause" else "Play"
        )
        Spacer(Modifier.width(4.dp))
        Text(if (inPlayback) "Pause" else "Play", maxLines = 1)
    }
}