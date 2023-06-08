package jti.jasminsa.gluecamera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import jti.jasminsa.gluecamera.databinding.ActivityResultBinding

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


    }
}