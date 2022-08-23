package com.example.newsapp.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import com.example.newsapp.R
import java.io.ByteArrayOutputStream

fun Uri.toBitmap(context: Context): Bitmap {
    try {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            MediaStore.Images.Media.getBitmap(
                context.contentResolver,
                this
            )
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, this)
            source.let { ImageDecoder.decodeBitmap(it) }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return BitmapFactory.decodeResource(
        context.resources,
        R.drawable.profile_image_placeholder)
}

fun Bitmap.toBase64(): String {
    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    val imageBytes = baos.toByteArray()
    return android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT)
}

fun String.toBitmap(): Bitmap {
    val imageBytes: ByteArray = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}