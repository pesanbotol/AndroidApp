//package com.pesanbotol.android.app.utility
//
//import android.content.Context
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.os.AsyncTask
//import kotlinx.coroutines.suspendCancellableCoroutine
//import java.net.HttpURLConnection
//import java.net.URL
//
//class ImageDownloader {
//
//    private suspend fun downloadBitmap(
//        context: Context,
//        imageUrl: String
//    ) = suspendCancellableCoroutine<Bitmap> { cont ->
//        Glide.with(context)
//            .asBitmap()
//            .load(image.imageFileUrl)
//            .listener(object : RequestListener<Bitmap> {
//                override fun onLoadFailed(
//                    e: GlideException, model: Any?,
//                    target: Target<Bitmap>?, isFirstResource: Boolean
//                ): Boolean {
//                    cont.resumeWith(Result.failure(e))
//                    return false
//                }
//
//                override fun onResourceReady(
//                    bitmap: Bitmap, model: Any?, target: Target<Bitmap>?,
//                    dataSource: DataSource?, isFirstResource: Boolean
//                ): Boolean {
//                    cont.resumeWith(Result.success(bitmap))
//                    return false
//                }
//            }).submit()
//    }
//}