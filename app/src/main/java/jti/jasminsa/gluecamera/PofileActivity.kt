package jti.jasminsa.gluecamera

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import jti.jasminsa.gluecamera.databinding.ActivityPofileBinding
import jti.jasminsa.gluecamera.databinding.ActivitySignUpBinding
import jti.jasminsa.gluecamera.ui.LoginActivity
import jti.jasminsa.gluecamera.ui.MainActivity
import jti.jasminsa.gluecamera.ui.ResultActivity

class PofileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPofileBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPofileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        binding.buttonl.setOnClickListener { signOut() }
        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    val intent = Intent(this, ResultActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun signOut() {
        auth.signOut()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}