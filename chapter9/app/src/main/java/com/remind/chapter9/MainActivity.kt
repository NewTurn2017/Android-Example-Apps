package com.remind.chapter9

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.remind.chapter9.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.playButton.setOnClickListener { mediaPlayerPlay() }
        binding.pauseButton.setOnClickListener { mediaPlayerPause() }
        binding.stopButton.setOnClickListener { mediaPlayerStop() }

    }

    private fun mediaPlayerPlay() {

        val intent = Intent(this, MediaPlayerService::class.java).apply {
            action = MEDIA_PLAYER_PLAY
        }
        startService(intent)

    }

    private fun mediaPlayerPause() {
        val intent = Intent(this, MediaPlayerService::class.java).apply {
            action = MEDIA_PLAYER_PAUSE
        }
        startService(intent)
    }

    private fun mediaPlayerStop() {
        val intent = Intent(this, MediaPlayerService::class.java).apply {
            action = MEDIA_PLAYER_STOP
        }
        startService(intent)
    }
}