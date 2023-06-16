package jti.jasminsa.gluecamera.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import jti.jasminsa.gluecamera.createCustomTempFile
import jti.jasminsa.gluecamera.databinding.ActivityResultBinding
import jti.jasminsa.gluecamera.rotateFile
import java.io.File

class result : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var auth: FirebaseAuth


    companion object{
        const val IMAGE = "image"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityResultBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        auth = FirebaseAuth.getInstance()
        Log.e("1 SINIIIIIIII", "SALAHNYA DI SINI:")
        if (auth.currentUser != null ){
            binding.button.setOnClickListener { startTakePhoto() }
            Log.e("2 SINIIIIIIII", "SALAHNYA DI SINI:")
        }else{
            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.setTitle("SORRY")
            alertDialog.setMessage("To see the result Signup or Login if u already have account ")
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SignUp"
            ) { dialog, which ->
                val moveIntent = Intent(this, SignUpActivity::class.java)
                startActivity(moveIntent)
                finish()
            }
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Login"
            ) { dialog, which ->
                val moveIntent = Intent(this, LoginActivity::class.java)
                startActivity(moveIntent)
                finish()
            }

            alertDialog.show()

            val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = 10f
            btnPositive.layoutParams = layoutParams
            btnNegative.layoutParams = layoutParams
        }
    }

    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            myFile.let { file ->
                rotateFile(file)
                binding.imageView.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@result,
                 "jti.jasminsa.glucamera",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

}