///* Copyright 2017 The TensorFlow Authors. All Rights Reserved.
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//==============================================================================*/
//package com.pesanbotol.android.app.utility
//
//import android.app.Application
//import android.graphics.Bitmap
//import android.os.SystemClock
//import android.util.Log
//import org.tensorflow.lite.Interpreter
//import org.tensorflow.lite.support.image.TensorImage
//import java.io.FileInputStream
//import java.io.IOException
//import java.nio.ByteBuffer
//import java.nio.ByteOrder
//import java.nio.MappedByteBuffer
//import java.nio.channels.FileChannel
//import java.util.*
//import java.util.AbstractMap.SimpleEntry
//
///** Classifies images with Tensorflow Lite.  */
//class ImageClassifier internal constructor(activity: Application) {
//    /* Preallocated buffers for storing image data in. */
//    private val intValues = IntArray(DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y)
//
//    /** An instance of the driver class to run model inference with Tensorflow Lite.  */
//    private var tflite: Interpreter?
//
//    /** Labels corresponding to the output of the vision model.  */
//    private val labelList: List<String>
//
//    /** A ByteBuffer to hold image data, to be feed into Tensorflow Lite as inputs.  */
//    private var imgData: ByteBuffer? = null
//
//    /** An array to hold inference results, to be feed into Tensorflow Lite as outputs.  */
//    private var labelProbArray: Array<FloatArray>? = null
//
//    /** multi-stage low pass filter  */
//    private var filterLabelProbArray: Array<FloatArray>? = null
//    private val sortedLabels = PriorityQueue<Map.Entry<String, Float>>(
//        RESULTS_TO_SHOW
//    ) { (_, value), (_, value1) -> value.compareTo(value1) }
//
//    /** Classifies a frame from the preview stream.  */
//    fun classifyFrame(bitmap: Bitmap): String {
//        if (tflite == null) {
//            Log.e(TAG, "Image classifier has not been initialized; Skipped.")
//            return "Uninitialized Classifier."
//        }
//        convertBitmapToByteBuffer(bitmap)
//        // Here's where the magic happens!!!
//        val startTime = SystemClock.uptimeMillis()
//        tflite!!.run(imgData, labelProbArray)
//        val endTime = SystemClock.uptimeMillis()
//        Log.d(
//            TAG,
//            "Timecost to run model inference: " + java.lang.Long.toString(endTime - startTime)
//        )
//
//        // smooth the results
//        applyFilter()
//
//        // print the results
//        var textToShow = printTopKLabels()
//        textToShow = java.lang.Long.toString(endTime - startTime) + "ms" + textToShow
//        return textToShow
//    }
//
//    fun applyFilter() {
//        val num_labels = labelList.size
//
//        // Low pass filter `labelProbArray` into the first stage of the filter.
//        for (j in 0 until num_labels) {
//            filterLabelProbArray!![0][j] += FILTER_FACTOR * (labelProbArray!![0][j] -
//                    filterLabelProbArray!![0][j])
//        }
//        // Low pass filter each stage into the next.
//        for (i in 1 until FILTER_STAGES) {
//            for (j in 0 until num_labels) {
//                filterLabelProbArray!![i][j] += FILTER_FACTOR * (filterLabelProbArray!![i - 1][j] -
//                        filterLabelProbArray!![i][j])
//            }
//        }
//
//        // Copy the last stage filter output back to `labelProbArray`.
//        for (j in 0 until num_labels) {
//            labelProbArray!![0][j] = filterLabelProbArray!![FILTER_STAGES - 1][j]
//        }
//    }
//
//    /** Closes tflite to release resources.  */
//    fun close() {
//        tflite!!.close()
//        tflite = null
//    }
//
//    /** Reads label list from Assets.  */
//    @Throws(IOException::class)
//    private fun loadLabelList(activity: Application): List<String> {
//        val labelList: MutableList<String> = ArrayList()
//        val labels = activity.assets.open("labels.txt").bufferedReader().use { it.readText() }
//            .split("\n")
//        labels.forEach {
//            labelList.add(it)
//        }
////        val reader = BufferedReader(InputStreamReader(activity.assets.open(LABEL_PATH)))
////        var line: String
////        while (reader.readLine().also { line = it } != null) {
////            labelList.add(line)
////        }
////        reader.close()
//        return labelList
//    }
//
//    /** Memory-map the model file in Assets.  */
//    @Throws(IOException::class)
//    private fun loadModelFile(activity: Application): MappedByteBuffer {
//        val fileDescriptor = activity.assets.openFd(MODEL_PATH)
//        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
//        val fileChannel = inputStream.channel
//        val startOffset = fileDescriptor.startOffset
//        val declaredLength = fileDescriptor.declaredLength
//        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
//    }
//
//    /** Writes Image data into a `ByteBuffer`.  */
//    private fun convertBitmapToByteBuffer(bitmap: Bitmap) {
//        if (imgData == null) {
//            return
//        }
//        imgData!!.clear()
//        val startTime = SystemClock.uptimeMillis()
////        val inputFeature0 =
////                TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
//
//        val tbuffer = TensorImage.fromBitmap(bitmap)
//        imgData = tbuffer.buffer
////        byteBuffer =
////        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
//        // Convert the image to floating point.
////        var pixel = 0
////
////        for (i in 0 until DIM_IMG_SIZE_X) {
////            for (j in 0 until DIM_IMG_SIZE_Y) {
////                val value = intValues[pixel++]
////                imgData!!.putFloat(((value shr 16 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
////                imgData!!.putFloat(((value shr 8 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
////                imgData!!.putFloat(((value and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
////            }
////        }
//        val endTime = SystemClock.uptimeMillis()
//        Log.d(
//            TAG,
//            "Timecost to put values into ByteBuffer: " + java.lang.Long.toString(endTime - startTime)
//        )
//    }
//
//    /** Prints top-K labels, to be shown in UI as the results.  */
//    private fun printTopKLabels(): String {
//        for (i in labelList.indices) {
//            sortedLabels.add(
//                SimpleEntry(labelList[i], labelProbArray!![0][i])
//            )
//            if (sortedLabels.size > RESULTS_TO_SHOW) {
//                sortedLabels.poll()
//            }
//        }
//        var textToShow = ""
//        val size = sortedLabels.size
//        for (i in 0 until size) {
//            val (key, value) = sortedLabels.poll()
//            textToShow = String.format("\n%s: %4.2f", key, value) + textToShow
//        }
//        return textToShow
//    }
//
//    companion object {
//        /** Tag for the [Log].  */
//        private const val TAG = "TfLiteCameraDemo"
//
//        /** Name of the model file stored in Assets.  */
//        private const val MODEL_PATH = "model.tflite"
//
//        /** Name of the label file stored in Assets.  */
//        private const val LABEL_PATH = "labels.txt"
//
//        /** Number of results to show in the UI.  */
//        private const val RESULTS_TO_SHOW = 3
//
//        /** Dimensions of inputs.  */
//        private const val DIM_BATCH_SIZE = 1
//        private const val DIM_PIXEL_SIZE = 1
//        const val DIM_IMG_SIZE_X = 448
//        const val DIM_IMG_SIZE_Y = 448
//        private const val IMAGE_MEAN = 448
//        private const val IMAGE_STD = 448.0f
//        private const val FILTER_STAGES = 3
//        private const val FILTER_FACTOR = 0.4f
//    }
//
//    /** Initializes an `ImageClassifier`.  */
//    init {
//        tflite = Interpreter(loadModelFile(activity))
//        labelList = loadLabelList(activity)
//        imgData = ByteBuffer.allocateDirect(
//            4 * DIM_BATCH_SIZE * DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y * DIM_PIXEL_SIZE
//        )
//        imgData?.order(ByteOrder.nativeOrder())
//        labelProbArray = Array(1) { FloatArray(labelList.size) }
//        filterLabelProbArray = Array(FILTER_STAGES) {
//            FloatArray(
//                labelList.size
//            )
//        }
//        Log.d(TAG, "Created a Tensorflow Lite Image Classifier.")
//    }
//}