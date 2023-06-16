package jti.jasminsa.gluecamera.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import jti.jasminsa.gluecamera.databinding.ActivityResultBinding

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

        if (auth.currentUser != null){
            if (intent.getByteArrayExtra(IMAGE) != null){
                val byteArray = intent.getByteArrayExtra(IMAGE)
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
                binding.imageView.setImageBitmap(bitmap)
            }
            binding.button.setOnClickListener { startTakePhoto() }
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

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val imageBitmap = it.data?.extras?.get("data") as Bitmap
            binding.imageView.setImageBitmap(imageBitmap)
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        launcherIntentCamera.launch(intent)
    }
}