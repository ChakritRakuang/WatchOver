package bombstudiothailandinc.watchover

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask

import com.squareup.okhttp.FormEncodingBuilder
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import com.squareup.okhttp.Response

class GetLocationWhereIdParent(@SuppressLint("StaticFieldLeak") private val context : Context) : AsyncTask<String , Void , String>() {

    override fun doInBackground(vararg strings : String) : String? {

        try {

            val okHttpClient = OkHttpClient()
            val requestBody = FormEncodingBuilder()
                    .add("isAdd" , "true")
                    .add("id_Parent" , strings[0])
                    .build()
            val builder = Request.Builder()
            val request = builder.url(urlPHP).post(requestBody).build()
            val response = okHttpClient.newCall(request).execute()
            return response.body().string()

        } catch (e : Exception) {
            e.printStackTrace()
            return null
        }
    }

    companion object {
        private val urlPHP = "http://androidthai.in.th/dom/getChildWhereIdParentAndLat.php"
    }
}   // Main Class