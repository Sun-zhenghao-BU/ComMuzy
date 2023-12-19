package com.example.commuzy.ui.me

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.commuzy.R
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class meFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var name_text: TextView
    private lateinit var reg_time_text: TextView
    private lateinit var edit_b: Button
    private lateinit var t_nickname:TextView
    private lateinit var t_bio:TextView
    private lateinit var t_age:TextView
    private var param2: String? = "b"
    private lateinit var gender_t: TextView
    private lateinit var i_avatar: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_me, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = auth.currentUser
        gender_t=view.findViewById<TextView>(R.id.me_t_gender)
//        name_text=view.findViewById<TextView>(R.id.me_name)
        reg_time_text=view.findViewById<TextView>(R.id.me_register_time)
        edit_b=view.findViewById<Button>(R.id.me_b_edit)
        t_age=view.findViewById<TextView>(R.id.t_age)
        t_bio=view.findViewById<TextView>(R.id.t_bio)
        t_nickname=view.findViewById<TextView>(R.id.t_nickname)
        i_avatar=view.findViewById<ImageView>(R.id.imageViewAvatar)
//        val registrationDate = user?.metadata?.creationTimestamp?.let {
//            Date(it).toString()
//        } ?: "Unknown"
        val registrationDate = user?.metadata?.creationTimestamp
        if (registrationDate != null) {
            val currentDate = Date()
            val registrationDateTime = Date(registrationDate)
            val daysRegistered = ((currentDate.time - registrationDateTime.time) / (1000 * 60 * 60 * 24)).toInt()
            val daysRegisteredText = "$daysRegistered days"
            reg_time_text.text = daysRegisteredText
        } else {
            reg_time_text.text = "Unknown"
        }

//        reg_time_text.text="$registrationDate"
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val name = currentUser.displayName?: "404"
//            name_text.text = "welcome, $name"?: "404"
        }
        // edit my file
        edit_b.setOnClickListener {
            findNavController().navigate(R.id.action_meFragment_to_EditFragment)
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        userId?.let {
            db.collection("users")
                .document(it)
                .get()
                .addOnSuccessListener { document ->
                if (document.exists()) {

                    val avatarUrl = document.getString("avatarUrl")
                    if (!avatarUrl.isNullOrEmpty()) {
                        Glide.with(this)
                            .load(avatarUrl)
                            .transform(CenterCrop(), RoundedCorners(20))
                            .into(i_avatar)
                    }
                    val birthday = document.getString("birthday") ?: "Unknown"
                    val age = if (birthday != "Unknown") calculateAge(birthday) else "Unknown"
                    t_age.text = "$age"

                    val bio = document.getString("bio") ?: "No Bio"
                    t_bio.text = "$bio"

                    val nickname = document.getString("nickname") ?: "No Nickname"
                    t_nickname.text = "$nickname"

                    val gender = document.getString("gender") ?: "Unknown"
                    gender_t.text = "$gender"


                } else {
                    gender_t.text = "Gender: edit me!"
                }
            }
        }
    }

    fun calculateAge(birthday: String): Int {
        val dob = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(birthday)
        val cal = Calendar.getInstance()
        val today = cal.time
        cal.time = dob
        var age = today.year - cal.time.year
        if (today.month < cal.time.month || (today.month == cal.time.month && today.day < cal.time.day)) {
            age--
        }
        return age
    }

}