package com.example.zombiehunter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Typeface
import android.os.Handler
import android.view.Display
import android.view.MotionEvent
import android.view.View

class SpaceShoot (context: Context) : View(context){

    private var background: Bitmap
    private var heartImage: Bitmap
    private val handler: Handler = Handler()
    private val UPDATE_MILLIS: Long = 30
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0
    private var points = 0
    private var life = 3
    private var highScore = 0
    private val scorePaint: Paint = Paint()
    private val TEXT_SIZE = 80f

    private val ourPlayer: Player
    private val enemyZombie: Zombie
    private val PlayerShoots: java.util.ArrayList<Boom> = java.util.ArrayList()
    private val ZombieShoots: java.util.ArrayList<Boom> = java.util.ArrayList()
    private val explosions: java.util.ArrayList<Explosion> = java.util.ArrayList()
    private var enemyShootAction = false
    private var playerInvincible = false
    private var lastUpdateTime = 0L
    private var gameEnded = false

    init {
        val display: Display = (context as Activity).windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenWidth = size.x
        screenHeight = size.y
        ourPlayer = Player(context, screenWidth, screenHeight)
        enemyZombie = Zombie(context)
        background = BitmapFactory.decodeResource(context.resources, R.drawable.game_play)
        background = Bitmap.createScaledBitmap(background, screenWidth, screenHeight, true)
        heartImage = BitmapFactory.decodeResource(context.resources, R.drawable.life1)


        val heartSize = screenWidth / 16
        heartImage = Bitmap.createScaledBitmap(heartImage, heartSize, heartSize, true)
        scorePaint.color = Color.GREEN
        scorePaint.textSize = TEXT_SIZE
        scorePaint.textAlign = Paint.Align.LEFT




        loadHighScore()
        startGame()
    }

    private fun startGame() {
        resetGame()
        handler.postDelayed(object : Runnable {
            override fun run() {
                updateGame()
                invalidate()
                handler.postDelayed(this, UPDATE_MILLIS)
            }
        }, UPDATE_MILLIS)
    }

    private fun resetGame() {
        points = 0
        life = 3
        PlayerShoots.clear()
        ZombieShoots.clear()
        explosions.clear()
        gameEnded = false
    }

    private fun updateGame() {
        if (!gameEnded) {
            val currentTime = System.currentTimeMillis()
            val elapsedTime = currentTime - lastUpdateTime

            if (elapsedTime >= UPDATE_MILLIS) {
                lastUpdateTime = currentTime

                if (!playerInvincible) {
                    // Update enemy movement
                    enemyZombie.ex += enemyZombie.enemyVelocity
                    if (enemyZombie.ex + enemyZombie.getEnemyZombieWidth() >= screenWidth || enemyZombie.ex <= 0) {
                        enemyZombie.enemyVelocity *= -1
                    }

                    // Trigger enemy shooting
                    if (!enemyShootAction) {
                        val shootDelay = 1000 // Delay between each witch shoot in milliseconds
                        handler.postDelayed({
                            val witchShoot = Boom(context, enemyZombie.ex + enemyZombie.getEnemyZombieWidth() / 2, enemyZombie.ey)
                            ZombieShoots.add(witchShoot)
                            enemyShootAction = false
                        }, shootDelay.toLong())
                        enemyShootAction = true
                    }

                    // Update witch shoots and check for collisions with player
                    val witchesToRemove: java.util.ArrayList<Boom> = java.util.ArrayList()
                    for (zombieShoot in ZombieShoots) {
                        zombieShoot.shy += 35 // Adjust the witch shoot speed as needed
                        if (zombieShoot.shy >= screenHeight) {
                            witchesToRemove.add(zombieShoot)
                        } else if (zombieShoot.shx + zombieShoot.getBoomWidth()>= ourPlayer.ox &&
                            zombieShoot.shx <= ourPlayer.ox + ourPlayer.getOurPlayerWidth() &&
                            zombieShoot.shy + zombieShoot.getBoomHeight() >= ourPlayer.oy &&
                            zombieShoot.shy <= ourPlayer.oy + ourPlayer.getOurPlayerHeight()) {
                            witchesToRemove.add(zombieShoot)
                            handlePlayerHit()
                        }
                    }
                   ZombieShoots.removeAll(witchesToRemove)
                }

                // Update wizard shoots and check for collisions with enemy witch
                updatePlayerShoots()

                // Check game end conditions
                checkGameEndConditions()
            }
        }
    }

    private fun handlePlayerHit() {
        if (!playerInvincible) {
            life--
            if (life <= 0) {
                endGame()
            } else {
                playerInvincible = true
                val explosion = Explosion(context, ourPlayer.ox, ourPlayer.oy)
                explosions.add(explosion)
                playerInvincible = false
            }
        }
    }

    private fun updatePlayerShoots() {
        val shootsToRemove: java.util.ArrayList<Boom> = java.util.ArrayList()
        for (playershoot in PlayerShoots) {
            playershoot.shy -= 25 // Adjust the shoot speed as needed
            if (playershoot.shy <= 0) {
                shootsToRemove.add(playershoot)
                continue
            }
            // Check for collision with enemy witch
            if (playershoot.shx + playershoot.getBoomWidth() >= enemyZombie.ex &&
                playershoot.shx <= enemyZombie.ex + enemyZombie.getEnemyZombieWidth() &&
                playershoot.shy + playershoot.getBoomHeight() >= enemyZombie.ey &&
                playershoot.shy <= enemyZombie.ey + enemyZombie.getEnemyZombieHeight() / 2) {
                shootsToRemove.add(playershoot)
                val explosion = Explosion(context, enemyZombie.ex, enemyZombie.ey)
                explosions.add(explosion)
                points++
                break
            }
        }
        PlayerShoots.removeAll(shootsToRemove)


        if (PlayerShoots.isEmpty()) {
            val ourShot = Boom(context, ourPlayer.ox + ourPlayer.getOurPlayerWidth()/ 2, ourPlayer.oy)
            PlayerShoots.add(ourShot)
        }
    }

    private fun checkGameEndConditions() {
        if (life <= 0 && !gameEnded) {
            endGame()
            gameEnded = true  // Set gameEnded flag to true here to avoid multiple calls to endGame()
        }
    }

    override fun onDraw(canvas: Canvas) {
        // Draw background
        canvas.drawBitmap(background, 0f, 0f, null)

        // Draw explosions
        val explosionsToRemove: java.util.ArrayList<Explosion> = java.util.ArrayList()
        for (explosion in explosions) {
            val explosionBitmap = explosion.getExplosion(explosion.explosionFrame) ?: continue
            canvas.drawBitmap(explosionBitmap, explosion.eX.toFloat(), explosion.eY.toFloat(), null)
            explosion.explosionFrame++
            if (explosion.explosionFrame > 8) {
                explosionsToRemove.add(explosion)
            }
        }
        explosions.removeAll(explosionsToRemove)

        // Draw score
        canvas.drawText("Score: $points", 0f, TEXT_SIZE, scorePaint)

        // Draw hearts
        for (i in life downTo 1) {
            canvas.drawBitmap(heartImage, (screenWidth - heartImage.width * i).toFloat(), 0f, null)
        }

        // Draw enemy witch
        canvas.drawBitmap(enemyZombie.getEnemyZombieBitmap(), enemyZombie.ex.toFloat(), enemyZombie.ey.toFloat(), null)

        // Draw our wizard
        canvas.drawBitmap(ourPlayer.getOurPlayer(), ourPlayer.ox.toFloat(), ourPlayer.oy.toFloat(), null)

        // Draw wizard shoots
        for (shoot in PlayerShoots) {
            canvas.drawBitmap(shoot.boomBitmap, shoot.shx.toFloat(), shoot.shy.toFloat(), null)
        }

        // Draw witch shoots
        for (shoot in ZombieShoots) {
            canvas.drawBitmap(shoot.boomBitmap, shoot.shx.toFloat(), shoot.shy.toFloat(), null)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x.toInt()

        if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
            ourPlayer.ox = touchX
        }

        if (event.action == MotionEvent.ACTION_UP) {
            val ourShot = Boom(context, ourPlayer.ox + ourPlayer.getOurPlayerWidth()/ 2, ourPlayer.oy)
            PlayerShoots.add(ourShot)
            val witchShot = Boom(context, enemyZombie.ex + enemyZombie.getEnemyZombieWidth() / 2, enemyZombie.ey)
            ZombieShoots.add(witchShot)
        }

        return true
    }


    private fun endGame() {
        if (points > highScore) {
            highScore = points
            saveHighScore()
        }
        handler.removeCallbacksAndMessages(null) // Remove all callbacks and messages
        val intent = Intent(context, GameOver::class.java)
        intent.putExtra("points", points)
        intent.putExtra("highScore", highScore)
        context.startActivity(intent)
        (context as Activity).finish()
    }

    private fun saveHighScore() {
        val prefs: SharedPreferences = context.getSharedPreferences("HighScorePrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putInt("highScore", highScore)
        editor.apply()
    }

    private fun loadHighScore() {
        val prefs: SharedPreferences = context.getSharedPreferences("HighScorePrefs", Context.MODE_PRIVATE)
        highScore = prefs.getInt("highScore", 0)
    }


}