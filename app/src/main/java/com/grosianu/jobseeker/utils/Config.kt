package com.grosianu.jobseeker.utils

class Config {
    var modelPath = DEFAULT_LOCAL_MODEL_PATH
    var inputLength = DEFAULT_INPUT_LENGTH
    var outputLength = DEFAULT_OUTPUT_LENGTH
    var topK = DEFAULT_TOP_K
    var postListPath = DEFAULT_POST_LIST_PATH
    var pad = PAD_ID
    var outputIdsIndex = DEFAULT_OUTPUT_IDS_INDEX
    var outputScoresIndex = DEFAULT_OUTPUT_SCORES_INDEX
    var favoriteListSize = DEFAULT_FAVORITE_LIST_SIZE
    var remoteModelName = REMOTE_MODEL_NAME

    companion object {
        private const val DEFAULT_LOCAL_MODEL_PATH = "recommendation_cnn_i10o100.tflite"
        private const val DEFAULT_POST_LIST_PATH = "sorted_post_vocab.json"
        private const val REMOTE_MODEL_NAME = "recommendations"
        private const val DEFAULT_INPUT_LENGTH = 10
        private const val DEFAULT_OUTPUT_LENGTH = 100
        private const val DEFAULT_TOP_K = 10
        private const val PAD_ID = 0
        private const val DEFAULT_OUTPUT_IDS_INDEX = 1
        private const val DEFAULT_OUTPUT_SCORES_INDEX = 0
        private const val DEFAULT_FAVORITE_LIST_SIZE = 100
    }
}