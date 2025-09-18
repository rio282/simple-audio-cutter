package nl.rio282.simple_audio_cutter.view


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nl.rio282.simple_audio_cutter.component.AudioInfoComponent
import nl.rio282.simple_audio_cutter.component.PlayPauseButton
import nl.rio282.simple_audio_cutter.component.TopBar
import nl.rio282.simple_audio_cutter.component.WaveFormDisplay
import nl.rio282.simple_audio_cutter.controller.AudioController
import nl.rio282.simple_audio_cutter.controller.AudioPlayer
import nl.rio282.simple_audio_cutter.model.AudioModel
import kotlin.system.exitProcess

@Composable
fun MainView() {
    var audio by remember { mutableStateOf<AudioModel?>(null) }
    var player by remember { mutableStateOf<AudioPlayer?>(null) }
    var inPlayback by remember { mutableStateOf(false) }

    var displayFileInfo by remember { mutableStateOf(false) }
    val maxPcmRenderSteps = 500

    var positions by remember { mutableStateOf<Pair<Long, Long>>(Pair(0, audio?.durationMs ?: 0)) }
    var steps by remember { mutableStateOf(Pair(0, maxPcmRenderSteps)) }

    Scaffold(
        topBar = {
            TopBar(
                onAudioImport = {
                    player?.stop()
                    audio = AudioController.pickFile() ?: audio
                    audio?.let { player = AudioPlayer(it.file) }
                },
                onExit = { exitProcess(0) }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when {
                audio == null -> {
                    Text("Please import an Audio file")
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = { displayFileInfo = !displayFileInfo },
                            modifier = Modifier.height(32.dp),
                            contentPadding = PaddingValues(
                                start = 8.dp,
                                top = 4.dp,
                                end = 8.dp,
                                bottom = 4.dp
                            )
                        ) {
                            Text("Show File Information", maxLines = 1)
                        }

                        AnimatedVisibility(
                            visible = displayFileInfo,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) { AudioInfoComponent(audio!!) }

                        WaveFormDisplay(
                            audio = audio!!,
                            positions = positions,
                            steps = steps,
                            maxPcmRenderSteps = maxPcmRenderSteps,
                            onPositionChange = {
                                positions = it
                                val left = ((it.first.toDouble() / audio!!.durationMs) * maxPcmRenderSteps).toInt()
                                val right = ((it.second.toDouble() / audio!!.durationMs) * maxPcmRenderSteps).toInt()
                                steps = Pair(left, right)
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        PlayPauseButton(
                            inPlayback = inPlayback,
                            onToggle = {
                                if (inPlayback) player?.pause()
                                else player?.play()
                                inPlayback = !inPlayback
                            }
                        )
                    }
                }
            }
        }
    }
}
