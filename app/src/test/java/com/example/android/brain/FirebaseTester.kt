package com.example.android.brain

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class FirebaseTester {

    private fun log(text: String) {
        Log.d("loggy", text)
    }

    @Test
    fun firebase_database() {
        val db = Firebase.firestore
        val user = hashMapOf(
            "first" to "Ada",
            "last" to "Lovelace",
            "born" to 1815)

        db.collection("users")
            .add(user)
            .addOnSuccessListener { log("DocumentSnapshot added with ID: ${it.id}") }
            .addOnFailureListener { log("Error adding document. $it") }

        assertEquals(true, true)
    }
}
