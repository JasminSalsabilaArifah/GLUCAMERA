package jti.jasminsa.gluecamera.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import jti.jasminsa.gluecamera.PofileActivity
import jti.jasminsa.gluecamera.R
import jti.jasminsa.gluecamera.createCustomTempFile
import jti.jasminsa.gluecamera.data.response.ResponseAnalis
import jti.jasminsa.gluecamera.data.retrofit.ApiConfig
import jti.jasminsa.gluecamera.databinding.ActivityResultBinding
import jti.jasminsa.gluecamera.reduceFileImage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var auth: FirebaseAuth
    private var getFile: File? = null

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
            binding.button.setOnClickListener {
                startTakePhoto()}
            binding.bottomNav.setOnItemSelectedListener {
                when(it.itemId){
                    R.id.profile -> {
                    val intent = Intent(this, PofileActivity::class.java)
                    startActivity(intent)
                    true
                    }
                    else -> false
                }
            }
        }else{
           trial()
        }
    }

    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            myFile.let { file ->
                //rotateFile(file)
                getFile = file
                binding.imageView.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
            //Log.e("siniiiiiiiiiiiiiiiiiiiiiiiiiii2", "findUser 1: siniiiiiiiiiiiiiiiiiiiiiiiiiii")
            uploadImage()
            //Log.e("siniiiiiiiiiiiiiiiiiiiiiiiiiii2", "findUser 1: siniiiiiiiiiiiiiiiiiiiiiiiiiii")
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@ResultActivity,
                 "jti.jasminsa.glucamera",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun trial(){
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

    private fun uploadImage() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            val apiService = ApiConfig().getApiService()
            val uploadImageRequest = apiService.uploadImage(imageMultipart)
            uploadImageRequest.enqueue(object : Callback<ResponseAnalis> {
                override fun onResponse(
                    call: Call<ResponseAnalis>,
                    response: Response<ResponseAnalis>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null ) {
                            Toast.makeText(this@ResultActivity, responseBody.result, Toast.LENGTH_SHORT).show()
                            Log.e("TAGTAGTAGTAGTAG 1", "TAGTAGTAGTAGTAGTAGTAGTAGTAGTAGTAGTAG ${file.length()}")
                        }
                    } else {
                        Toast.makeText(this@ResultActivity, response.message(), Toast.LENGTH_SHORT).show()
                        Log.e("TAGTAGTAGTAGTAG 2", "TAGTAGTAGTAGTAGTAGTAGTAGTAGTAGTAGTAG ${file.length()}")

                    }
                }
                override fun onFailure(call: Call<ResponseAnalis>, t: Throwable) {
                    Toast.makeText(this@ResultActivity, t.message, Toast.LENGTH_SHORT).show()
                    Log.e("TAGTAGTAGTAGTAG 3", "TAGTAGTAGTAGTAGTAGTAGTAGTAGTAGTAGTAG ${file.length()}")

                }
            })
        } else {
            Toast.makeText(this@ResultActivity, "Take Picture First", Toast.LENGTH_SHORT).show()
        }
    }

}