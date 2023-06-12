package jti.jasminsa.gluecamera

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import jti.jasminsa.gluecamera.databinding.ActivityResultBinding
import java.io.ByteArrayOutputStream

class result : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    companion object{
        const val IMAGE = "image"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityResultBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val byteArray = intent.getByteArrayExtra(IMAGE)
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
        Log.e("TAG", "findUser 1: ${bitmap}")

        binding.imageView.setImageBitmap(bitmap)

        binding.button.setOnClickListener { startTakePhoto() }
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