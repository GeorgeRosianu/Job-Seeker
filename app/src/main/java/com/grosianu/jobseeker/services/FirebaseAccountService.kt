package com.grosianu.jobseeker.services
//
//import android.util.Log
//import com.google.firebase.crashlytics.FirebaseCrashlytics
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.FirebaseFirestoreException
//import com.google.firebase.firestore.QuerySnapshot
//import com.grosianu.jobseeker.models.Application
//import com.grosianu.jobseeker.models.Application.Companion.toApplication
//import com.grosianu.jobseeker.models.User
//import com.grosianu.jobseeker.models.User.Companion.toUser
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.cancel
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.callbackFlow
//import kotlinx.coroutines.tasks.await
//
//object FirebaseAccountService {
//
//    private const val TAG = "FirebaseProfileService"
//
//    suspend fun getAccountData(userId: String): User? {
//
//        val database = FirebaseFirestore.getInstance()
//
//        return try {
//            database.collection("users")
//                .document(userId).get().await().toUser()
//        } catch (e: Exception) {
//            Log.e(TAG, "Error getting user details", e)
//            FirebaseCrashlytics.getInstance().log("Error getting user details")
//            FirebaseCrashlytics.getInstance().setCustomKey("userId", userId)
//            FirebaseCrashlytics.getInstance().recordException(e)
//            null
//        }
//    }
//
////    suspend fun getUsers(userId: String): List<User> {
////
////        val database = FirebaseFirestore.getInstance()
////
////        return try {
////            db.collection("users")
////                .document(userId)
////                .collection("friends").get().await()
////                .documents.mapNotNull { it.toUser() }
////        } catch (e: Exception) {
////            Log.e(TAG, "Error getting user friends", e)
////            FirebaseCrashlytics.getInstance().log("Error getting user friends")
////            FirebaseCrashlytics.getInstance().setCustomKey("userId", userId)
////            FirebaseCrashlytics.getInstance().recordException(e)
////            emptyList()
////        }
////    }
//
//    @ExperimentalCoroutinesApi
//    fun getApplications(userId: String): Flow<List<Application>> {
//
//        val database = FirebaseFirestore.getInstance()
//
//        return callbackFlow {
//            val listenerRegistration = database.collection("users")
//                .document(userId)
//                .collection("applications")
//                .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
//                    if (firebaseFirestoreException != null) {
//                        cancel(
//                            message = "Error fetching applications",
//                            cause = firebaseFirestoreException
//                        )
//                        return@addSnapshotListener
//                    }
//                    val map = querySnapshot?.documents?.mapNotNull { it.toApplication() }
//                    if (map != null) {
//                        trySend(map)
//                    }
//                }
//            awaitClose {
//                Log.d(TAG, "Cancelling posts listener")
//                listenerRegistration.remove()
//            }
//        }
//    }
//}