package com.grosianu.jobseeker.ui.home.destinations.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.toObject
import com.grosianu.jobseeker.models.News
import kotlinx.coroutines.launch

class NotificationsViewModel : ViewModel() {

    private var _newsList = MutableLiveData<List<News>>()
    val newsList: LiveData<List<News>> = _newsList

    private var _news = MutableLiveData<News>()
    val news: LiveData<News> = _news

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var _hasNews = MutableLiveData(false)
    val hasNews: LiveData<Boolean> = _hasNews

    fun getContent() {
        viewModelScope.launch {
            val docRef = db.collection("news")
            docRef.whereEqualTo("userId", auth.currentUser?.uid)
                .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    val newsArray = ArrayList<News>()
                    if (querySnapshot != null) {
                        for (document in querySnapshot) {
                            if (document != null && document.exists()) {
                                val newsTemp: News = document.toObject()
                                newsTemp.seen = true
                                newsArray.add(newsTemp)
                            }
                        }
                    }
                    _newsList.value = newsArray
                    _hasNews.value = !newsArray.isNullOrEmpty()
                }
        }
    }

    companion object {
        private const val TAG = "NEWS_VIEW_MODEL"
    }
}