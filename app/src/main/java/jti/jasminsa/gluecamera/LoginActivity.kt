package jti.jasminsa.gluecamera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jti.jasminsa.gluecamera.databinding.ActivityLoginBinding
import jti.jasminsa.gluecamera.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}