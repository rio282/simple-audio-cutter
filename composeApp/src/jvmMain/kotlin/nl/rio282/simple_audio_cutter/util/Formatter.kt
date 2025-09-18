package nl.rio282.simple_audio_cutter.util

fun formatMsTime(ms: Long): String {
    val minutes = (ms / 60000)
    val seconds = (ms % 60000) / 1000
    val hundredths = (ms % 1000) / 10
    return "%02d:%02d.%02d".format(minutes, seconds, hundredths)
}
