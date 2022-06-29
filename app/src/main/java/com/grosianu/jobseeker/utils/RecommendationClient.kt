package com.grosianu.jobseeker.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager
import com.google.firebase.ml.custom.FirebaseCustomRemoteModel
import com.grosianu.jobseeker.models.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.util.HashMap

class RecommendationClient(private val context: Context, private val config: Config) {
    private val candidates: MutableMap<Int, Post> = HashMap()
    private var tflite: Interpreter? = null

    data class Result(
        val id: Int,
        val item: Post,
        val confidence: Float
    ) {
        override fun toString(): String {
            return String.format("[%d] confidence: %.3f, item: %s", id, confidence, item)
        }
    }

    suspend fun load() {
        downloadRemoteModel()
        loadLocalModel()
        loadCandidateList()
    }

    private suspend fun loadLocalModel() {
        return withContext(Dispatchers.IO) {
            try {
                val buffer: ByteBuffer = FileUtils.loadModelFile(
                    context.assets, config.modelPath
                )
                initializeInterpreter(buffer)
                Log.v(TAG, "TFLite model loaded.")
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            }
        }
    }

    private suspend fun initializeInterpreter(model: Any) {
        return withContext(Dispatchers.IO) {
            tflite?.apply {
                close()
            }
            tflite = if (model is ByteBuffer) {
                Interpreter(model)
            } else {
                Interpreter(model as File)
            }
            Log.v(TAG, "TFLite model loaded.")
        }
    }

    private suspend fun loadCandidateList() {
        return withContext(Dispatchers.IO) {
            val collection = listOf<Post>() // PostsRepository.getInstance(context).getContent()
            for (item in collection) {
                candidates[item.id?.toInt()!!] = item
            }
            Log.v(TAG, "Candidate list loaded.")
        }
    }

    fun unload() {
        tflite?.close()
        candidates.clear()
    }

    @Synchronized
    suspend fun recommend(selectedPosts: List<Post>): List<Result> {
        return withContext(Dispatchers.Default) {
            val inputs = arrayOf<Any>(preprocess(selectedPosts))

            val outputIds = IntArray(config.outputLength)
            val confidences = FloatArray(config.outputLength)
            val outputs: MutableMap<Int, Any> = HashMap()
            outputs[config.outputIdsIndex] = outputIds
            outputs[config.outputScoresIndex] = confidences
            tflite?.let {
                it.runForMultipleInputsOutputs(inputs, outputs)
                postprocess(outputIds, confidences, selectedPosts)
            } ?: run {
                Log.e(TAG, "No tflite interpreter loaded")
                emptyList()
            }
        }
    }

    @Synchronized
    private suspend fun preprocess(selectedPosts: List<Post>): IntArray {
        return withContext(Dispatchers.Default) {
            val inputContext = IntArray(config.inputLength)
            for (i in 0 until config.inputLength) {
                if (i < selectedPosts.size) {
                    val (id) = selectedPosts[i]
                    inputContext[i] = id?.toInt()!!
                } else {
                    inputContext[i] = config.pad
                }
            }
            inputContext
        }
    }

    @Synchronized
    private suspend fun postprocess(
        outputIds: IntArray, confidences: FloatArray, selectedPosts: List<Post>
    ): List<Result> {
        return withContext(Dispatchers.Default) {
            val results = ArrayList<Result>()

            for (i in outputIds.indices) {
                if (results.size >= config.topK) {
                    Log.v(TAG, String.format("Selected top K: %d. Ignore the rest.", config.topK))
                    break
                }
                val id = outputIds[i]
                val item = candidates[id]
                if (item == null) {
                    Log.v(TAG, String.format("Inference output[%d]. Id: %s is null", i, id))
                    continue
                }
                if (selectedPosts.contains(item)) {
                    Log.v(TAG, String.format("Inference output[%d]. Id: %s is contained", i, id))
                    continue
                }
                val result = Result(
                    id, item,
                    confidences[i]
                )
                results.add(result)
                Log.v(TAG, String.format("Inference output[%d]. Result: %s", i, result))
            }
            results
        }
    }

    private fun downloadRemoteModel() {
        downloadModel(config.remoteModelName)
    }

    private fun downloadModel(modelName: String) {
        val remoteModel = FirebaseCustomRemoteModel.Builder(modelName).build()
        val firebaseModelManager = FirebaseModelManager.getInstance()
        firebaseModelManager
            .isModelDownloaded(remoteModel)
            .continueWithTask { task ->
                val conditions = if (task.result != null && task.result == true) {
                    FirebaseModelDownloadConditions.Builder()
                        .requireWifi()
                        .build()
                } else {
                    FirebaseModelDownloadConditions.Builder().build();
                }
                firebaseModelManager.download(remoteModel, conditions)
            }
            .addOnSuccessListener {
                firebaseModelManager.getLatestModelFile(remoteModel)
                    .addOnCompleteListener {
                        val model = it.result
                        if (model == null) {
                            Toast.makeText(
                                context,
                                "Failed to get model file.",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Downloaded remote model",
                                Toast.LENGTH_LONG
                            ).show()
                            GlobalScope.launch { initializeInterpreter(model) }
                        }
                    }
            }
            .addOnFailureListener {
                Toast.makeText(
                    context,
                    "Model download failed for recommendations, please check your connection.",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    companion object {
        private const val TAG = "RecommendationClient"
    }
}