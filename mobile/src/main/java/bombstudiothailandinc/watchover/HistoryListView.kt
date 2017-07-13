package bombstudiothailandinc.watchover

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView

import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request

import org.json.JSONArray

import java.util.ArrayList

class HistoryListView : AppCompatActivity() {

    private var listView : ListView? = null
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_list_view)

        listView = findViewById<View>(R.id.listView2) as ListView?
        val connectedHistory = ConnectedHistory()
        connectedHistory.execute()
    } //Main Method

    @SuppressLint("StaticFieldLeak")
    inner class ConnectedHistory : AsyncTask<Void , Void , String>() {

        override fun doInBackground(vararg params : Void) : String? {

            try {

                val okHttpClient = OkHttpClient()
                val builder = Request.Builder()
                val request = builder.url("").build()
                val response = okHttpClient.newCall(request).execute()
                return response.body().string()
            } catch (e : Exception) {
                e.printStackTrace()
                return null
            }

        } //doInBack

        override fun onPostExecute(s : String) {
            super.onPostExecute(s)

            try {

                val jsonArray = JSONArray(s)

                val allTimeStrings = arrayOfNulls<String>(jsonArray.length())
                val allDateStrings = arrayOfNulls<String>(jsonArray.length())

                val stringArrayList = ArrayList<String>()

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    allTimeStrings[i] = jsonObject.getString("Date")
                    val resultStrings = allTimeStrings[i] !!.split("".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    allDateStrings[i] = resultStrings[0]
                    allDateStrings[i]?.let { stringArrayList.add(it) }

                    Log.d("24April" , "Date(" + i + ") =" + allDateStrings[i])

                }

                val objects = stringArrayList.toTypedArray() as Array<*>
                objects
                        .filter { stringArrayList.indexOf(it) != stringArrayList.lastIndexOf(it) }
                        .forEach { stringArrayList.removeAt(stringArrayList.lastIndexOf(it)) }//if

                val stringArrayAdapter = ArrayAdapter(this@HistoryListView ,
                        android.R.layout.simple_list_item_1 , stringArrayList)
                listView !!.adapter = stringArrayAdapter

            } catch (e : Exception) {
                e.printStackTrace()
            }

        } //onPost
    } //connectedClass
} //Main Class