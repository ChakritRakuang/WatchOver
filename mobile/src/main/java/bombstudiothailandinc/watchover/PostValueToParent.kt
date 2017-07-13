package bombstudiothailandinc.watchover

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.squareup.okhttp.FormEncodingBuilder
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request

class PostValueToParent(@SuppressLint("StaticFieldLeak") private val context : Context ,
                        private val nameString : String ,
                        private val userString : String ,
                        private val passwordString : String ,
                        private val genderString : String) : AsyncTask<Void , Void , String>() {

    override fun doInBackground(vararg params : Void) : String? {

        try {
            val okHttpClient = OkHttpClient()
            val requestBody = FormEncodingBuilder()
                    .add("isAdd" , "true")
                    .add("Name" , nameString)
                    .add("User" , userString)
                    .add("Password" , passwordString)
                    .add("Gender" , genderString)
                    .build()
            val builder = Request.Builder()
            val request = builder.url(urlPHP).post(requestBody).build()
            val response = okHttpClient.newCall(request).execute()
            return response.body().string()

        } catch (e : Exception) {
            Log.d("12MarchV1" , "e doIn ==>" + e.toString())
            return null
        }
    }

    companion object {
        private val urlPHP = "http://androidthai.in.th/dom/addParentMind.php"
    }
}//Main class
