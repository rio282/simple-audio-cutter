package nl.rio282.simple_audio_cutter.controller

import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory
import nl.rio282.simple_audio_cutter.model.AudioModel
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

object AudioController {

    fun pickFile(): AudioModel? {
        val chooser = JFileChooser().apply {
            dialogTitle = "Select an Audio File"
            fileFilter = FileNameExtensionFilter("Audio files", "mp3", "wav", "flac", "ogg")
            isAcceptAllFileFilterUsed = false
        }
        if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) return null
        val file = chooser.selectedFile

        // metadata
        val audioFile = AudioFileIO.read(file)
        val tag = audioFile.tag
        val title = tag?.getFirst(FieldKey.TITLE)
        val artist = tag?.getFirst(FieldKey.ARTIST)
        val album = tag?.getFirst(FieldKey.ALBUM)
        val durationMs = audioFile.audioHeader.trackLength * 1000L

        val samples = decodeToPCM(file)

        return AudioModel(
            file = file,
            title = title,
            artist = artist,
            album = album,
            durationMs = durationMs,
            pcmSamples = samples
        )
    }

    private fun decodeToPCM(file: File): ShortArray {
        val dispatcher = AudioDispatcherFactory.fromPipe(
            file.absolutePath,
            44100, // sample rate
            2048,  // buffer size
            0      // overlap
        )
        val buffer = mutableListOf<Float>()

        dispatcher.addAudioProcessor(object : AudioProcessor {
            override fun process(audioEvent: AudioEvent?): Boolean {
                audioEvent?.floatBuffer?.forEach { buffer.add(it) }
                return true
            }
            override fun processingFinished() {}
        })

        dispatcher.run()

        return buffer.map {
            (it * Short.MAX_VALUE).toInt()
                .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                .toShort()
        }.toShortArray()
    }
}