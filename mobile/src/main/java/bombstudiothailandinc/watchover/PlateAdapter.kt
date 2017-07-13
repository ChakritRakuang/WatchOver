package bombstudiothailandinc.watchover

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class PlateAdapter(//Explicit
        private val context : Context , private val titleStrings : Array<String>)//Constructor
    : BaseAdapter() {

    override fun getCount() : Int {
        return titleStrings.size
    }

    override fun getItem(position : Int) : Any? {
        return null
    }

    override fun getItemId(position : Int) : Long {
        return 0
    }

    @SuppressLint("ViewHolder" , "SetTextI18n")
    override fun getView(i : Int , view : View , viewGroup : ViewGroup) : View {

        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view1 = layoutInflater.inflate(R.layout.plate_listview , viewGroup , false)

        val numTextView = view1.findViewById<View>(R.id.textView8) as TextView //เปลี่ยนเป็น textview อัตโนมัติ
        val strNum = Integer.toString(i + 1)
        numTextView.text = strNum + "."

        val titleTextView = view1.findViewById<View>(R.id.textView9) as TextView
        titleTextView.text = titleStrings[i]


        return view1
    }
}// main class