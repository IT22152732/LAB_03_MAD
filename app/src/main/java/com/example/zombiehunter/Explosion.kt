package com.example.zombiehunter

// Importing necessary Android libraries
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory


// Define the Explosion class to manage the explosion animations
class Explosion(private val context: Context,var eX: Int, var eY: Int) {


    // Array to hold Bitmap references for each frame of the explosion animation
    private val explosion = arrayOfNulls<Bitmap>(9)
    // This variable keeps track of the current frame of the explosion animation.
// It is initialized to 0, meaning the animation will start with the first frame.
    var explosionFrame: Int = 0



    init {
        // Decode the every explosion frame from resources and assign it to the their index of the explosion array.
        explosion[0] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion0)
        explosion[1] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion1)
        explosion[2] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion2)
        explosion[3] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion3)
        explosion[4] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion4)
        explosion[5] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion5)
        explosion[6] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion6)
        explosion[7] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion7)
        explosion[8] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion8)

        // Initialize explosionFrame to 0. Note: This local declaration is unnecessary as explosionFrame is already declared as a class member.
        var explosionFrame = 0
    }


    /**
     * Returns the Bitmap image of the explosion frame corresponding to the specified index.
     * This method allows accessing any frame in the explosion sequence by providing its index.
     *
     * @param explosionFrame The index of the explosion frame to retrieve.
     * @return The Bitmap of the specified explosion frame or null if the index is out of range.
     */
    fun getExplosion(explosionFrame: Int): Bitmap? {
        return explosion[explosionFrame]
    }
}
