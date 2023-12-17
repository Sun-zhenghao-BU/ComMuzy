package com.example.commuzy

import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

open class UserAuthBaseActivity : AppCompatActivity() {
    private var progressBar: ProgressBar? = null

    val uid: String
        get() = FirebaseAuth.getInstance().currentUser!!.uid

    fun setProgressBar(resId: Int) {
        progressBar = findViewById(resId)
    }

    fun showProgressBar() {
        progressBar?.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar?.visibility = View.INVISIBLE
    }
}