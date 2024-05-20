package com.example.zombiehunter

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class StartUp : AppCompatActivity() {





    private lateinit var tvHighScore: TextView



    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.startup)









        // Initialize TextView for high score
        tvHighScore = findViewById(R.id.tvHighScore)

        // Retrieve the high score from SharedPreferences
        val highScore = getHighScore()

        // Update the TextView to display the high score
        tvHighScore.text = getString(R.string.high_score, highScore)


        mediaPlayer = MediaPlayer.create(this, R.raw.malith_start_sound)
        mediaPlayer?.start()
    }

    // Method to retrieve high score from SharedPreferences
    private fun getHighScore(): Int {
        val prefs = getSharedPreferences("HighScorePrefs", MODE_PRIVATE)
        return prefs.getInt("highScore", 0) // Default value is 0
    }

    fun startGame(v: View) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release() // Release the media player resources
        mediaPlayer = null
    }
}
