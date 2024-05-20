package com.example.zombiehunter

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * The GameOver activity class displays the final score and high score to the user,
 * and provides options to restart the game or exit.
 */
class GameOver : AppCompatActivity() {

    private lateinit var tvPoints: TextView// TextView for displaying points
    private lateinit var tvHighScore: TextView// TextView for displaying high score
    private var mediaPlayer: MediaPlayer? = null// MediaPlayer to play game over sound

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gameover)// Set the layout for this activity

        // Retrieve points and high score from the intent that started this activity
        val points = intent.getIntExtra("points", 0) // Retrieve points from intent extras
        val highScore = intent.getIntExtra("highScore", 0) // Retrieve high score from intent extras

        // Initialize and set the points TextView
        tvPoints = findViewById(R.id.tvPoints)
        tvPoints.text = getString(R.string.points, points) // Display points

        // Initialize and set the high score TextView
        tvHighScore = findViewById(R.id.tvHighScore) // Initialize TextView for high score
        tvHighScore.text = getString(R.string.high_score, highScore) // Display high score

        // Create and start the MediaPlayer to play the game over sound
        mediaPlayer = MediaPlayer.create(this, R.raw.oversound)
        mediaPlayer?.start()// Start playing the sound

    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release() // Release media player resources
        mediaPlayer = null
    }

    /**
     * Restarts the game by starting the StartUp activity and finishing this one.
     */
    fun restart(view: View) {
        val intent = Intent(this, StartUp::class.java)
        startActivity(intent)
        finish()
    }

    fun exit(view: View) {
        finishAffinity()// Close all activities and exit the app
    }
}
