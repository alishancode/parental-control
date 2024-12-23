package com.example.limit.managers

import android.content.Context
import android.media.AudioManager

class AudioManager(private val context: Context) {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    fun stopAudioPlayback() {
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true)
    }
}
