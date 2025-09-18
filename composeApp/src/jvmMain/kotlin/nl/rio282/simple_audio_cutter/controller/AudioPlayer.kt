package nl.rio282.simple_audio_cutter.controller

import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory
import be.tarsos.dsp.io.jvm.AudioPlayer as TarsosAudioPlayer
import java.io.File
import javax.sound.sampled.AudioFormat
import kotlin.concurrent.thread

class AudioPlayer(private val file: File) {
    private var dispatcher: AudioDispatcher? = null
    private var thread: Thread? = null
    private var isPlaying = false

    fun play() {
        if (isPlaying) return

        val bufferSize = 2048
        val overlap = 0

        // match sample rate, sample size, and channels etc.
        val sampleRate = 44100f
        val sampleSizeBits = 16
        val channels = 1
        val signed = true
        val bigEndian = false

        val audioFormat = AudioFormat(sampleRate, sampleSizeBits, channels, signed, bigEndian)

        dispatcher = AudioDispatcherFactory.fromPipe(
            file.absolutePath,
            sampleRate.toInt(),
            bufferSize,
            overlap
        ).apply {
            addAudioProcessor(TarsosAudioPlayer(audioFormat))
        }

        thread = thread(start = true, isDaemon = true) {
            isPlaying = true
            try {
                dispatcher?.run()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isPlaying = false
            }
        }
    }

    fun pause() {
        dispatcher?.stop()
        isPlaying = false
    }

    fun stop() {
        dispatcher?.stop()
        thread?.interrupt()
        isPlaying = false
    }

    fun isPlaying(): Boolean = isPlaying
}
