package bombstudiothailandinc.watchover

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask

import com.google.android.gms.common.api.Response
import com.squareup.okhttp.FormEncodingBuilder
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody

class PostChildToServer(@SuppressLint("StaticFieldLeak") private val context : Context) : AsyncTask<String , Void , String>() {

    override fun doInBackground(vararg strings : String) : String? {

        try {

            val okHttpClient = OkHttpClient()
            val requestBody = FormEncodingBuilder()
                    .add("isAdd" , "true")
                    .add("Code" , strings[0])
                    .add("Name" , strings[1])
                    .add("Gender" , strings[2])
                    .add("id_Parent" , strings[3])
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
        private val urlPHP = "http://androidthai.in.th/dom/addDataChan.php"
    }
}