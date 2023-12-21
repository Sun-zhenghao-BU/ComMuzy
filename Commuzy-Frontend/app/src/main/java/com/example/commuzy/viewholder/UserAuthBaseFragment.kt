package com.example.commuzy.viewholder
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

open class UserAuthBaseFragment: Fragment() {
    private var progressBar: ProgressBar? = null

    val uid: String
//        get() = Firebase.auth.currentUser!!.uid
        get() = FirebaseAuth.getInstance().currentUser!!.uid

    fun setProgressBar(resId: Int) {
        progressBar = view?.findViewById(resId)
    }

    fun showProgressBar() {
        progressBar?.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar?.visibility = View.INVISIBLE
    }
}