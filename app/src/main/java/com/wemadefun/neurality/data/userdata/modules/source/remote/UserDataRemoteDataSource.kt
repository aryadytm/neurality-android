package com.wemadefun.neurality.data.userdata.modules.source.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wemadefun.neurality.data.userdata.UserData
import com.wemadefun.neurality.data.userdata.modules.source.UserDataProcessor
import com.wemadefun.neurality.data.userdata.modules.source.UserDataSource
import com.wemadefun.neurality.utils.CONFIG_DB_PATH_USERDATA
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import com.wemadefun.neurality.utils.SECOND
import kotlinx.coroutines.delay

enum class Status {
    FETCHING, EXISTS, NOT_EXISTS, ERROR
}

object UserDataRemoteDataSource : UserDataSource {

    private val db: FirebaseFirestore
        get() = Firebase.firestore

    private var onUpdateFailure: () -> Unit = {}
    private var onUpdateSuccess: () -> Unit = {}
    private var onFetchError: () -> Unit = {}
    private var onUserdataNotExists: () -> Unit = {}
    private var onUserdataExists: (UserData) -> Unit = {}

    fun setOnRemoteUpdateFailure(listener: () -> Unit) { onUpdateFailure = listener }
    fun setOnRemoteUpdateSuccess(listener: () -> Unit) { onUpdateSuccess = listener }
    fun setOnRemoteFetchError(listener: () -> Unit) { onFetchError = listener }
    fun setOnRemoteUserdataNotExists(listener: () -> Unit) { onUserdataNotExists = listener }
    fun setOnRemoteFetchSuccess(listener: (UserData) -> Unit) { onUserdataExists = listener }

    private var fetchedUserData: UserData? = null
    private var fetchedUserDataStatus: Status = Status.FETCHING

    init {
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
        db.firestoreSettings = settings
    }

    override suspend fun getUserData(uid: String): UserData {
        val data = fetchedUserData
        return data!!
    }

    override suspend fun isUserDataExists(uid: String): Status {
        fetchedUserDataStatus = Status.FETCHING
        fetchedUserData = null
        fetchRemoteUserData(uid)

        var timePassedMs = 0L
        while (fetchedUserDataStatus == Status.FETCHING) {
            delay(100)
            timePassedMs += (100)

            if (timePassedMs > SECOND * 15) {
                fetchedUserDataStatus = Status.ERROR
                break
            }
        }

        return fetchedUserDataStatus
    }

    private fun userDataExists(userDataHashMap: HashMap<String, Any>) {
        val userDataJson = UserDataProcessor.getUserDataJson(userDataHashMap)
        val userData = UserDataProcessor.getUserDataFromJson(userDataJson)
        fetchedUserData = userData
        onUserdataExists(userData)
    }

    override suspend fun updateUserData(uid: String, userData: UserData) {
        val dataJson = UserDataProcessor.getUserDataJson(userData)
        val dataHashMap = UserDataProcessor.getHashMapFromJson(dataJson)
        val collection = db.collection(CONFIG_DB_PATH_USERDATA).document(uid)

        collection.set(dataHashMap, SetOptions.merge())
            .addOnSuccessListener {
                FireCrashlytics.log("Remote UserData updated. Play Count: ${dataHashMap["playCount"]}")
                onUpdateSuccess()
            }
            .addOnFailureListener {
                FireCrashlytics.log("Remote UserData update failed. Going offline mode. ($it)")
                FireCrashlytics.report(it)
                onUpdateFailure()
            }
    }

    private fun fetchRemoteUserData(uid: String) {
        val collection = db.collection(CONFIG_DB_PATH_USERDATA).document(uid)

        collection.get(Source.SERVER).addOnSuccessListener { document ->
            if (document.get("uid") != null) {
                userDataExists(document.data as HashMap<String, Any>)
                fetchedUserDataStatus = Status.EXISTS
            }
            else {
                onUserdataNotExists()
                fetchedUserDataStatus = Status.NOT_EXISTS
            }
        }.addOnFailureListener {
            FireCrashlytics.report(it)
            onFetchError()
            fetchedUserDataStatus = Status.ERROR
        }
    }
}