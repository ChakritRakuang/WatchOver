package bombstudiothailandinc.watchover

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.util.Log

import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response

class GetAllData(@SuppressLint("StaticFieldLeak") private val context : Context) : AsyncTask<String , Void , String>() {

    override fun doInBackground(vararg strings : String) : String? {

        try {

            val okHttpClient = OkHttpClient()
            val builder = Request.Builder()
            val request = builder.url(strings[0]).build()
            val response = okHttpClient.newCall(request).execute()
            return response.body().string()

        } catch (e : Exception) {
            Log.d("4JuneV1" , "e doIn ==> " + e.toString())
            return null
        }

    }
}   // Main Class