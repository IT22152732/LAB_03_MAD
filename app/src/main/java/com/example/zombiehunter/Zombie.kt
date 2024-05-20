package com.example.zombiehunter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.*

class Zombie(private val context: Context) {
    private var enemyZombie: Bitmap
    var ex = 0
    var ey = 0
    var enemyVelocity = 0
    private val random: Random

    init {
        val options = BitmapFactory.Options()
        options.inSampleSize = 2 // Adjust this value to resize the image accordingly
        enemyZombie = BitmapFactory.decodeResource(context.resources, R.drawable.zombie, options)
        random =Random()
        resetEnemyZombie()
    }

    fun getEnemyZombieBitmap(): Bitmap {
        return enemyZombie
    }

    fun getEnemyZombieWidth(): Int {
        return  enemyZombie.width
    }

    fun getEnemyZombieHeight(): Int {
        return enemyZombie.height
    }

    private fun resetEnemyZombie() {
        ex = 200 + random.nextInt(400)
        ey = 0
        enemyVelocity = 17 + random.nextInt(10)
    }
}