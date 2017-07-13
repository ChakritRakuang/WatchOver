package bombstudiothailandinc.watchover

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

class MyAlert(//ประกาศตัวแปร
        private val context : Context) {

    fun myDialog(strTitle : String , strMessage : String) {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setIcon(INT)
        builder.setTitle(strTitle)
        builder.setMessage(strMessage)
        builder.setPositiveButton("OK") { dialog , _ -> dialog.dismiss() }
        builder.show()
    }

    companion object {
        private val INT = R.mipmap.name
    }
}//Main Class