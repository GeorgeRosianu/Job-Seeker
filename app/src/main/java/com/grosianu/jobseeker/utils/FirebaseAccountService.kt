package com.grosianu.jobseeker.utils

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.grosianu.jobseeker.models.User
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

object FirebaseAccountService {
    private const val TAG = "FirebaseAccountService"

//    suspend fun getAccountData(userId: String): User? {
//        val db = FirebaseFirestore.getInstance()
//        return try {
//            db.collection("users").document(userId).get().await().toUser()
//        } catch (e: Exception) {
//            Log.e(TAG, "Error getting user data", e)
//            null
//        }
//    }
//
//    fun getUserData(userId: String): Flow<User> {
//        val db = FirebaseFirestore.getInstance()
//        return callbackFlow {
//            val listenerRegistration = db.collection("users")
//                .document(userId)
//                .addSnapshotListener { document, firebaseFirestoreException ->
//                    if (firebaseFirestoreException != null) {
//                        cancel(message = "Error fetching posts", cause = firebaseFirestoreException)
//                        return@addSnapshotListener
//                    }
//
//                    if (document != null && document.exists()) {
//                        val user = document.toUser()!!
//                        trySend(user)
//                    }
//                }
//            awaitClose {
//                Log.d(TAG, "Cancelling account listener")
//                listenerRegistration.remove()
//            }
//        }
//    }
}