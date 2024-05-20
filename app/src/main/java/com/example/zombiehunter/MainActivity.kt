package com.example.zombiehunter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.media.MediaPlayer



//Initializes the game view and sets it as the content view on creation.
class MainActivity : AppCompatActivity() {

    private lateinit var backgroundMusicPlayer: MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the XML layout for the activity. This layout might contain basic UI structure.
        setContentView(R.layout.activity_main)


        // Create and start background music player
        backgroundMusicPlayer = MediaPlayer.create(this, R.raw.boomsound)
        backgroundMusicPlayer.isLooping = true // Set looping
        backgroundMusicPlayer.start()


        // Create an instance of the game view, SpaceShoot, passing the current context.
        val witchHunterView = SpaceShoot(this)

        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,// Width will match the parent element, filling the screen.
            ViewGroup.LayoutParams.MATCH_PARENT// Height will also match the parent element, filling the screen
        )
        // Add the game view to the activity's view hierarchy with the specified layout parameters
        addContentView(witchHunterView, layoutParams)
    }

    override fun onResume() {
        super.onResume()
        if (!backgroundMusicPlayer.isPlaying) {
            backgroundMusicPlayer.start()
        }
    }

    override fun onPause() {
        super.onPause()
        if (backgroundMusicPlayer.isPlaying) {
            backgroundMusicPlayer.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        backgroundMusicPlayer.release()
    }
}

