package com.office.meong.data.presigned.local.datasource

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject
import kotlin.math.max
import androidx.core.graphics.scale

class ImageLocalDataSource @Inject constructor(
    @param: ApplicationContext private val context: Context,
) {
    suspend fun getOptimizedFile(uriString: String): File = withContext(Dispatchers.IO) {
        val uri = uriString.toUri()
        val dir = getDirectory()
        compressToWebP(uri, dir)
    }

    fun clearCache() {
        getDirectory().listFiles()?.forEach { it.delete() }
    }

    private fun getDirectory(): File {
        return File(context.cacheDir, DIRECTORY).apply {
            if (!exists()) mkdirs()
        }
    }

    private fun compressToWebP(uri: Uri, dir: File): File {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            decodeWithImageDecoder(uri)
        } else {
            decodeWithBitmapFactory(uri)
        }

        val format = if (Build.VERSION.SDK_INT >= 30) {
            Bitmap.CompressFormat.WEBP_LOSSY
        } else {
            Bitmap.CompressFormat.WEBP
        }

        val byteArray = ByteArrayOutputStream().use { stream ->
            bitmap.compress(format, WEBP_QUALITY, stream)
            bitmap.recycle()
            stream.toByteArray()
        }

        val tempFile = File(dir, "${UUID.randomUUID()}.webp")
        FileOutputStream(tempFile).use { it.write(byteArray) }

        return tempFile
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun decodeWithImageDecoder(uri: Uri): Bitmap {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        return ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            decoder.isMutableRequired = true

            val size = info.size
            val targetSize = calculateTargetSize(size.width, size.height)
            decoder.setTargetSize(targetSize.first, targetSize.second)
        }
    }

    private fun decodeWithBitmapFactory(uri: Uri): Bitmap {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, bounds)
        }

        val (targetWidth, targetHeight) = calculateTargetSize(bounds.outWidth, bounds.outHeight)
        val decodeOptions = BitmapFactory.Options().apply {
            inSampleSize = calculateInSampleSize(bounds.outWidth, bounds.outHeight, targetWidth, targetHeight)
        }
        val sampledBitmap = context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, decodeOptions)
        } ?: error("이미지를 디코딩할 수 없습니다")

        val scaledBitmap = sampledBitmap.scale(targetWidth, targetHeight)
        if (scaledBitmap !== sampledBitmap) sampledBitmap.recycle()
        return scaledBitmap
    }

    private fun calculateTargetSize(width: Int, height: Int): Pair<Int, Int> {
        if (width <= MAX_SIZE && height <= MAX_SIZE) return width to height

        val ratio = max(width.toFloat() / MAX_SIZE, height.toFloat() / MAX_SIZE)
        return (width / ratio).toInt() to (height / ratio).toInt()
    }

    private fun calculateInSampleSize(width: Int, height: Int, reqWidth: Int, reqHeight: Int): Int {
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    companion object {
        private const val DIRECTORY = "image_cache"
        private const val MAX_SIZE = 1024
        private const val WEBP_QUALITY = 80
    }
}
