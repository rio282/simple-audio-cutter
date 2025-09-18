package nl.rio282.simple_audio_cutter.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onAudioImport: () -> Unit,
    onExit: () -> Unit,
) {
    TopAppBar(
        title = { Text("Simple Audio Cutter", color = Color.White) },
        actions = {
            TextButton(onClick = onAudioImport) { Text("Import Audio", color = Color.White) }
            TextButton(onClick = onExit) { Text("Exit", color = Color.White) }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Black,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
