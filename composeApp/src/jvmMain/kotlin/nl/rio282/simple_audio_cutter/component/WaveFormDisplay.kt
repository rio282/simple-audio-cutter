package nl.rio282.simple_audio_cutter.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import nl.rio282.simple_audio_cutter.model.AudioModel
import nl.rio282.simple_audio_cutter.util.formatMsTime
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaveFormDisplay(
    audio: AudioModel,
    positions: Pair<Long, Long>,
    steps: Pair<Int, Int>,
    maxPcmRenderSteps: Int,
    onPositionChange: (Pair<Long, Long>) -> Unit
) {
    // precompute waveform samples (reduce to ~maxSteps points for display)
    val samples = remember(audio) {
        val step = (audio.pcmSamples.size / maxPcmRenderSteps.toDouble()).coerceAtLeast(1.0)
        List(maxPcmRenderSteps) { i ->
            val index = (i * step).roundToInt().coerceAtMost(audio.pcmSamples.size - 1)
            audio.pcmSamples[index] / Short.MAX_VALUE.toFloat()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val newStep = (offset.x / size.width * maxPcmRenderSteps).toInt()
                        val newPosition =
                            (newStep.toDouble() / maxPcmRenderSteps * audio.durationMs).toLong()
                        // TODO: you could snap the nearest thumb to this newPosition
                    }
                }
        ) {
            val widthPerSample = size.width / samples.size
            val centerY = size.height / 2
            for ((i, sample) in samples.withIndex()) {
                val x = i * widthPerSample
                val y = sample * centerY
                drawLine(
                    color = if (i >= steps.first && i <= steps.second) Color.Blue else Color.Gray,
                    start = Offset(x, centerY - y),
                    end = Offset(x, centerY + y),
                    strokeWidth = 1f
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        RangeSlider(
            value = positions.first.toFloat()..positions.second.toFloat(),
            onValueChange = { range ->
                onPositionChange(range.start.toLong() to range.endInclusive.toLong())
            },
            valueRange = 0f..audio.durationMs.toFloat(),
            steps = 0,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(formatMsTime(positions.first))
            Text(formatMsTime(audio.durationMs))
            Text(formatMsTime(positions.second))
        }
    }
}
