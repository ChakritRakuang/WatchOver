package bombstudiothailandinc.watchover

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class HomeActivity : AppCompatActivity() , View.OnClickListener {

    //Explict
    private var button : Button? = null
    private var textView : TextView? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initialView()

        clickController()

    }// Main Method

    private fun clickController() {
        button !!.setOnClickListener(this@HomeActivity)
        textView !!.setOnClickListener(this@HomeActivity)
    }

    private fun initialView() {
        button = findViewById<View>(R.id.btnContHome) as Button
        textView = findViewById<View>(R.id.txtSignInHome) as TextView

    }

    override fun onClick(view : View) {
        //For Button
        if (view === button) {
            startActivity(Intent(this@HomeActivity , CreateAccountActivity::class.java))
        }

        //For TextView
        if (view === textView) {
            startActivity(Intent(this@HomeActivity , LoginActivity::class.java))
        }

    }// OnClick
} // Main Class