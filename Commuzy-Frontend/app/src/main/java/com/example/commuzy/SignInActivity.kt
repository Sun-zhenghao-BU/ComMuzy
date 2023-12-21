package com.example.commuzy

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import com.example.commuzy.databinding.ActivitySignInBinding


import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.commuzy.datamodel.UserInfo
import com.example.commuzy.models.User
import com.example.commuzy.ui.Auth.SignInFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInActivity : UserAuthBaseActivity () {
    private lateinit var binding: ActivitySignInBinding

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var buttonSignIn: Button
    private lateinit var buttonSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window) {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = ContextCompat.getColor(this@SignInActivity, R.color.black)
            navigationBarColor = ContextCompat.getColor(this@SignInActivity, R.color.black)
        }
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        buttonSignIn=findViewById(R.id.buttonSignIn)
        buttonSignUp=findViewById(R.id.buttonSignUp)
        setProgressBar(R.id.progressBar) // 根据你的进度条ID调整

        // 点击监听器
        with(binding) {

            buttonSignIn.setOnClickListener { signIn() }
            buttonSignUp.setOnClickListener { signUp() }
        }
    }

    override fun onStart() {
        super.onStart()

        // Check auth on Activity start
        auth.currentUser?.let {
            onAuthSuccess(it)
        }
    }

    private fun signIn() {
        Log.d(SignInFragment.TAG, "signIn")
        if (!validateForm()) {
            return
        }

        showProgressBar()
        val email = binding.fieldEmail.text.toString()
        val password = binding.fieldPassword.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                Log.d(SignInFragment.TAG, "signIn:onComplete:" + task.isSuccessful)
                hideProgressBar()

                if (task.isSuccessful) {
                    onAuthSuccess(task.result?.user!!)
                } else {
                    Toast.makeText(
                        this,
                        "Sign In Failed",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun signUp() {
        Log.d(SignInFragment.TAG, "signUp")
        if (!validateForm()) {
            return
        }

        showProgressBar()
        val email = binding.fieldEmail.text.toString()
        val password = binding.fieldPassword.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                Log.d(SignInFragment.TAG, "createUser:onComplete:" + task.isSuccessful)
                hideProgressBar()

                if (task.isSuccessful) {
                    onAuthSuccess(task.result?.user!!)
                } else {
                    Toast.makeText(
                        this,
                        "Sign Up Failed",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun onAuthSuccess(user: FirebaseUser) {
        val username = usernameFromEmail(user.email!!)

        // Write new user
        val newUser = UserInfo(id = user.uid, name = username, email = user.email!!)
        val appDatabase = (application as MainApplication).database

        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.databaseDao().insertUser(newUser)
        }

        writeNewUser(user.uid, username, user.email)

        // Go to MainActivity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun usernameFromEmail(email: String): String {
        return if (email.contains("@")) {
            email.split("@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        } else {
            email
        }
    }

    private fun validateForm(): Boolean {
        var result = true
        if (TextUtils.isEmpty(binding.fieldEmail.text.toString())) {
            binding.fieldEmail.error = "Required"
            result = false
        } else {
            binding.fieldEmail.error = null
        }

        if (TextUtils.isEmpty(binding.fieldPassword.text.toString())) {
            binding.fieldPassword.error = "Required"
            result = false
        } else {
            binding.fieldPassword.error = null
        }

        return result
    }

    private fun writeNewUser(userId: String, name: String, email: String?) {
        val user = User(name, email)
        database.child("users").child(userId).setValue(user)
    }
}