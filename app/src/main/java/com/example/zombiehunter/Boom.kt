package com.example.zombiehunter

// Necessary Android imports
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect

// Define the Boom class to manage the explosion bitmap and its properties
class Boom(context: Context, var shx: Int, var shy: Int) {

    // Bitmap to hold the explosion image
    var boomBitmap: Bitmap

    init {
        // Options to adjust the decoding properties
        val options = BitmapFactory.Options()
        // Reduce memory usage by loading a smaller version of the image
        options.inSampleSize = 4
        // Decode the boom resource image with the specified options
        boomBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.boom,options)

    }

    // Function to return the bitmap
    fun getBoom(): Bitmap {
        return boomBitmap
    }

    // Function to get the width of the boom bitmap
    fun getBoomWidth(): Int {
        return boomBitmap.width
    }

    // Function to get the height of the boom bitmap
    fun getBoomHeight(): Int {
        return boomBitmap.height
    }

    // Function to calculate and return the hitbox as a Rect
    fun getHitbox(): Rect {
        // Create and return a rectangle from (shx, shy) to (shx + width, shy + height)
        return Rect(shx, shy, shx + boomBitmap.width, shy + boomBitmap.height)
    }
}