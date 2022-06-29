package com.grosianu.jobseeker.utils

import android.content.res.AssetManager
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

object FileUtils {

    @Throws(IOException::class)
    fun loadModelFile(assetManager: AssetManager, modelPath: String?): MappedByteBuffer {
        assetManager.openFd(modelPath!!).use { fileDescriptor ->
            FileInputStream(fileDescriptor.fileDescriptor).use { inputStream ->
                val fileChannel = inputStream.channel
                val startOffset = fileDescriptor.startOffset
                val declaredLength = fileDescriptor.declaredLength

                return fileChannel.map(
                    FileChannel.MapMode.READ_ONLY,
                    startOffset,
                    declaredLength
                )
            }
        }
    }
}