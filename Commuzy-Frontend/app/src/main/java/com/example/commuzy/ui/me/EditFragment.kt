package com.example.commuzy.ui.me

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.commuzy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale


class EditFragment : Fragment() {
    private lateinit var spinnerGender: Spinner
    private lateinit var b_cancel:Button
    private lateinit var b_submit:Button
    private lateinit var birth_et: EditText
    private lateinit var bio_et:EditText
    private lateinit var nickname_et:EditText
    private lateinit var avatar_ib:ImageButton
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                uploadImageToFirebaseStorage(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val genders = listOf("Male", "Female", "Other","Wish Not to Say")
        val textColor:Int= Color.WHITE
        val backgroundColor:Int= Color.GRAY
        val adapter = context?.let { CustomGenderSpinnerAdapter(it, android.R.layout.simple_spinner_item, genders, textColor,backgroundColor) }
//        val adapter = ArrayAdapter(requireContext(), android.R.layout.custom_spinner_item, genders)
        b_cancel=view.findViewById<Button>(R.id.edit_button_cancel)
        b_submit=view.findViewById<Button>(R.id.buttonSubmit)
        spinnerGender=view.findViewById(R.id.spinnerGender)
        birth_et=view.findViewById<EditText>(R.id.edit_text_birthday)
        bio_et=view.findViewById<EditText>(R.id.edit_text_bio)
        nickname_et=view.findViewById<EditText>(R.id.edit_text_nickname)
        avatar_ib=view.findViewById<ImageButton>(R.id.buttonUploadAvatar)
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let { uid ->
            db.collection("users").document(uid).get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val avatarUrl = document.getString("avatarUrl")
                    if (!avatarUrl.isNullOrEmpty()) {
                        Glide.with(this)
                            .load(avatarUrl)
                            .placeholder(R.drawable.avatar_default)
                            .error(R.drawable.avatar_default)
                            .into(avatar_ib)
                    } else {
                        avatar_ib.setImageResource(R.drawable.avatar_default)
                    }
                }
            }.addOnFailureListener {
                avatar_ib.setImageResource(R.drawable.avatar_default)
            }
        }


        avatar_ib.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }



        b_cancel.setOnClickListener {
            findNavController().navigate(R.id.action_EditFragment_to_meFragment)
        }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGender.adapter = adapter

        spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedView = view as? TextView
                selectedView?.setTextColor(Color.WHITE)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        b_submit.setOnClickListener {
            val gender = spinnerGender.selectedItem.toString()
            val birthday = if (birth_et.text.isNotEmpty()) birth_et.text.toString() else "Not Specified"
            val birthdayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            birthdayFormat.isLenient = false

            val isValidBirthday = try {
                birthdayFormat.parse(birthday)
                true
            } catch (e: ParseException) {
                false
            }
            if (!isValidBirthday) {
                birth_et.text.clear()
                Toast.makeText(context, "Wrong format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val bio = if (bio_et.text.isNotEmpty()) bio_et.text.toString() else "No Bio"
            val nickname = if (nickname_et.text.isNotEmpty()) nickname_et.text.toString() else "No nickname"

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val db = FirebaseFirestore.getInstance()
            userId?.let {
                db.collection("users")
                    .document(it)
                    .set(mapOf("gender" to gender, "birthday" to birthday, "bio" to bio, "nickname" to nickname))
                    .addOnSuccessListener {
                        findNavController().navigate(R.id.action_EditFragment_to_meFragment)
                }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }

        }
    }

    private fun uploadImageToFirebaseStorage(imageUri: Uri) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let { uid ->
            val storageRef = FirebaseStorage.getInstance().reference.child("avatars/$uid.jpg")
            storageRef.putFile(imageUri).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    Glide.with(this)
                        .load(imageUrl)
                        .transform(CenterCrop(), RoundedCorners(20))
                        .into(avatar_ib)
                    FirebaseFirestore.getInstance().collection("users").document(uid)
                        .update("avatarUrl", imageUrl)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Update succeed", Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Update failed: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}