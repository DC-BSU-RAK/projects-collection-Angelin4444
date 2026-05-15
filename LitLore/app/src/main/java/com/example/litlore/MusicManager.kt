package com.example.litlore

import android.content.Context
import android.media.MediaPlayer

// This is the main DJ for the app. It handles all the background tunes.
object MusicManager {

    // Creating a spot to hold the music player so it can be controlled from anywhere.
    private var mediaPlayer: MediaPlayer? = null

    // Time to get the vibes going!
    fun startMusic(context: Context) {

        // Only start it up if nothing is playing yet.
        if (mediaPlayer == null) {

            // Grabbing the audio file from the raw folder.
            mediaPlayer = MediaPlayer.create(context, R.raw.bg_music)

            // Keeping the track on repeat forever.
            mediaPlayer?.isLooping = true

            // Pressing play.
            mediaPlayer?.start()
        }
    }

    // Taking a quick break from the tunes.
    fun pauseMusic() {

        // Just hitting pause.
        mediaPlayer?.pause()
    }

    // Bringing the beat back!
    fun resumeMusic() {

        // Hitting play again.
        mediaPlayer?.start()
    }

    // Shutting it all down completely.
    fun stopMusic() {

        // Stopping the track.
        mediaPlayer?.stop()

        // Freeing up phone memory so the app stays fast.
        mediaPlayer?.release()

        // Clearing the spot so it is ready for next time.
        mediaPlayer = null
    }
}