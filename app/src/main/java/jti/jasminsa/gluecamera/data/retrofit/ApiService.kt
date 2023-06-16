package jti.jasminsa.gluecamera.data.retrofit

import jti.jasminsa.gluecamera.data.response.ResponseAnalis
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("predicts")
    fun uploadImage(
        @Part file: MultipartBody.Part,
    ): Call<ResponseAnalis>
}

