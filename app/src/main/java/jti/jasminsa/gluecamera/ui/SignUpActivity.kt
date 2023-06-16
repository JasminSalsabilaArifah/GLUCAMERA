package jti.jasminsa.gluecamera.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import jti.jasminsa.gluecamera.databinding.ActivitySignUpBinding


class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
//    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
//        db = FirebaseFirestore.getInstance()

        binding.btnSignup.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        binding.btnSignup.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            when {
                name.isEmpty() -> {
                    binding.etlName.error = "Fill the name"
                }
                email.isEmpty() -> {
                    binding.etlEmail.error = "Fill the email"
                }
                password.isEmpty() -> {
                    binding.etlPassword.error = "Fill the password"
                }
                else -> {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                        if (it.isSuccessful) {
//                            val user: MutableMap<String, Any> = HashMap()
//                            user["Name"] = name
//                            user["Email"] = email
//                            user["Password"] = password
//                            db.collection("users")
//                                .add(user)
//                                .addOnSuccessListener { documentReference ->
//                                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
//                                    val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
//                                    startActivity(intent)
//                                }
//                                .addOnFailureListener { e ->
//                                    Log.w("TAG", "Error adding document", e)
//                                }

                            Toast.makeText(this, "Successfully Singed Up", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, "Singed Up Failed!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

}