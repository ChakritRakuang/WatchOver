package bombstudiothailandinc.watchover

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import org.json.JSONArray
import org.json.JSONObject

class LoginActivity : AppCompatActivity() , View.OnClickListener {

    private val urlJSONString = "http://androidthai.in.th/dom/getParent.php"
    private var userEditText : EditText? = null
    private var passwordEditText : EditText? = null
    private var button : Button? = null
    private var forgotTextView : TextView? = null
    private var createAccountTextView : TextView? = null
    private var userString : String? = null
    private var passwordString : String? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Initial View
        initialView()

        //Controller
        controller()

    }   // Main Method

    private fun controller() {
        button !!.setOnClickListener(this)
        forgotTextView !!.setOnClickListener(this)
        createAccountTextView !!.setOnClickListener(this)
    }

    private fun initialView() {
        userEditText = findViewById<View>(R.id.edtUser) as EditText
        passwordEditText = findViewById<View>(R.id.edtPassword) as EditText
        button = findViewById<View>(R.id.btnLogin) as Button
        forgotTextView = findViewById<View>(R.id.txtForgot) as TextView
        createAccountTextView = findViewById<View>(R.id.txtCreateAccount) as TextView
    }

    override fun onClick(view : View) {

        //Create Account
        if (view === createAccountTextView) {
            val intent = Intent(this@LoginActivity , CreateAccountActivity::class.java)
            startActivity(intent)
        }

        //For Login
        if (view === button) {
            checkUserAndPass()
        }

    }   // onClick

    private fun checkUserAndPass() {

        //Get Value from Edit Text
        userString = userEditText !!.text.toString().trim { it <= ' ' }
        passwordString = passwordEditText !!.text.toString().trim { it <= ' ' }

        val myAlert = MyAlert(this@LoginActivity)

        //Check Space
        if (userString == "" || passwordString == "") {
            myAlert.myDialog("Have Space" , "Please Fill All Every Blank")
        } else {
            synUserAndPass()
        }
    }

    private fun synUserAndPass() {

        try {

            val myAlert = MyAlert(this@LoginActivity)
            val getAllData = GetAllData(this@LoginActivity)
            getAllData.execute(urlJSONString)
            val strJSON = getAllData.get()
            Log.d("4JuneV1" , "JSON ==> " + strJSON)
            val columnStrings = arrayOf("id" , "Name" , "User" , "Password" , "Gender")
            val loginStrings = arrayOfNulls<String>(columnStrings.size)

            var b = true
            val jsonArray = JSONArray(strJSON)
            var i = 0
            while (i < jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                if (userString == jsonObject.getString("User")) {
                    b = false
                    var i1 = 0
                    while (i1 < columnStrings.size) {
                        loginStrings[i1] = jsonObject.getString(columnStrings[i1])
                        Log.d("4JuneV1" , "login(" + i1 + ") ==> " + loginStrings[i1])
                        i1 += 1
                    }
                }
                i += 1
            }

            if (b) {
                myAlert.myDialog("User False" , "No This User in my Database")
            } else if (passwordString == loginStrings[3]) {
                Toast.makeText(this@LoginActivity , "Welcome " + loginStrings[1] ,
                        Toast.LENGTH_SHORT).show()

                val intent = Intent(this@LoginActivity , MyServiceActivity::class.java)
                intent.putExtra("Login" , loginStrings)
                startActivity(intent)
                finish()

            } else {
                myAlert.myDialog("Password False" , "Please Try Again Password False")
            }

        } catch (e : Exception) {
            Log.d("4JuneV1" , "e check ==> " + e.toString())
        }
    }
}   // Main Class