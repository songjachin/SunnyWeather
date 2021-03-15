package com.songjachin.weather

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.Source
import java.io.File
import java.io.IOException

fun main() {
    run()
}

interface ProgressListener {
    fun update(bytesRead: Long, contentLength: Long, done: Boolean)
}

private fun run() {
    val request = Request.Builder()
        .url("https://images.pexels.com/photos/5177790/pexels-photo-5177790.jpeg")
        .build()
    val progressListener: ProgressListener = object : ProgressListener {
        var firstUpdate = true
        override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
            if (done) {
                println("completed")
            } else {
                if (firstUpdate) {
                    firstUpdate = false
                    if (contentLength == -1L) {
                        println("content-length: unknown")
                    } else {
                        System.out.format("content-length: %d\n", contentLength)
                    }
                }
                println(bytesRead)
                if (contentLength != -1L) {
                    System.out.format("%d%% done\n", 100 * bytesRead / contentLength)
                }
            }
        }
    }
    val client = OkHttpClient.Builder()
        .addNetworkInterceptor { chain: Interceptor.Chain ->
            val originalResponse = chain.proceed(chain.request())
            originalResponse.newBuilder()
               // .body(ProgressResponseBody(originalResponse.body!!, progressListener))
                .build()
        }
        .build()
    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) {
            throw IOException("Unexpected code $response")
        }
        //val desktopDir = FileSystemView.getFileSystemView().homeDirectory
        //val imageFile = File(desktopDir, "${System.currentTimeMillis()}.jpeg")
       // imageFile.createNewFile()
        //读取 InputStream 写入到图片文件中
        //response.body!!.byteStream().copyTo(imageFile.outputStream())
    }
}

private class ProgressResponseBody constructor(
    private val responseBody: ResponseBody,
    private val progressListener: ProgressListener
) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
           // bufferedSource = source(responseBody.source()).buffer()
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {

        return object : ForwardingSource(source) {

            var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1L)
                return bytesRead
            }
        }
    }

}