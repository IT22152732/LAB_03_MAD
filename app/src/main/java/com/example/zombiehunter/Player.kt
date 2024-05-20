package com.example.zombiehunter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import java.util.Random

class Player(private val context: Context, private val screenWidth: Int, private val screenHeight: Int) {

    private var ourPlayer: Bitmap
    var ox: Int = 0
    var oy: Int = 0
    var isAlive: Boolean = true
    private var playerVelocity: Int = 0

    private val random: Random = Random()

    init {
        val options = BitmapFactory.Options()
        options.inSampleSize = 2
        ourPlayer = BitmapFactory.decodeResource(context.resources, R.drawable.player, options)
        resetPlayer()
    }

    fun getOurPlayer(): Bitmap {
        return ourPlayer
    }

    fun getOurPlayerWidth(): Int {
        return ourPlayer.width
    }

    fun getOurPlayerHeight(): Int {
        return ourPlayer.height
    }

    private fun resetPlayer() {
        ox = random.nextInt(screenWidth)
        oy = screenHeight - ourPlayer.height
        playerVelocity = 10 + random.nextInt(6)
    }

    fun move(x: Int) {
        // Implement movement logic here if needed
    }

    fun getHitbox(): Rect {
        return Rect(ox, oy, ox + getOurPlayerWidth(), oy + getOurPlayerHeight())
    }
}
