package bombstudiothailandinc.watchover

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast

class CreateAccountActivity : AppCompatActivity() , View.OnClickListener {

    //Explicit
    private var namEditText : EditText? = null
    private var userEditText : EditText? = null
    private var passwordEditText : EditText? = null
    private var rePasswordEditText : EditText? = null
    private var radioGroup : RadioGroup? = null
    private var maleRadioButton : RadioButton? = null
    private var femaleRadioButton : RadioButton? = null
    private var button : Button? = null
    private var nameString : String? = null
    private var userString : String? = null
    private var passwordString : String? = null
    private var rePasswordString : String? = null
    private val genderString : String? = null

    private var anInt : Int = 0 //0==>male 1==>female


    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        initialView()

        buttonController()

        radioController()

        //Back
        backController()

    }//main method

    private fun backController() {
        val imageView = findViewById<View>(R.id.imvBack) as ImageView
        imageView.setOnClickListener { finish() }
    }

    private fun radioController() {
        radioGroup !!.setOnCheckedChangeListener { _ , checkedId ->
            when (checkedId) {
                R.id.radMale -> anInt = 0
                R.id.radFemale -> anInt = 1
            }
        }
    }

    private fun buttonController() {

        button !!.setOnClickListener(this@CreateAccountActivity)
    }

    private fun initialView() {
        namEditText = findViewById<View>(R.id.edtName) as EditText
        userEditText = findViewById<View>(R.id.edtUser) as EditText
        passwordEditText = findViewById<View>(R.id.edtPassword) as EditText
        rePasswordEditText = findViewById<View>(R.id.edtRepassword) as EditText
        radioGroup = findViewById<View>(R.id.ragGender) as RadioGroup
        maleRadioButton = findViewById<View>(R.id.radMale) as RadioButton
        femaleRadioButton = findViewById<View>(R.id.radFemale) as RadioButton
        button = findViewById<View>(R.id.btnContCreateAccount) as Button

    }

    override fun onClick(v : View) {
        //get Value Edit Text
        nameString = namEditText !!.text.toString().trim { it <= ' ' }
        userString = userEditText !!.text.toString().trim { it <= ' ' }
        passwordString = passwordEditText !!.text.toString().trim { it <= ' ' }
        rePasswordString = rePasswordEditText !!.text.toString().trim { it <= ' ' }

        //check space
        if (nameString == "" ||
                userString == "" ||
                passwordString == "" ||
                rePasswordString == "") {
            //Have space

            val myAlert = MyAlert(this@CreateAccountActivity)
            myAlert.myDialog("มีช่องว่าง" , "กรุณากรอกทุกช่อง")
        } else if (passwordString != rePasswordString) {
            //Password not match
            val myAlert = MyAlert(this@CreateAccountActivity)
            myAlert.myDialog("Password ไม่ตรงกัน" , "กรุณากรอกให้ตรงกันด้วย")
        } else if (maleRadioButton !!.isChecked || femaleRadioButton !!.isChecked) {
            //Check Radio button
            upLoadValueToServer()

        } else {
            //Non check
            val myAlert = MyAlert(this@CreateAccountActivity)
            myAlert.myDialog("ยังไม่เลือก Gender" , "กรุณาเลือก Gender")
        }

    }//onClick

    private fun upLoadValueToServer() {

        try {

            val postValueToParent = nameString?.let {
                userString?.let { it1 ->
                    passwordString?.let { it2 ->
                        PostValueToParent(this@CreateAccountActivity ,
                                it , it1 , it2 , Integer.toString(anInt))
                    }
                }
            }
            postValueToParent?.execute()
            if (java.lang.Boolean.parseBoolean(postValueToParent?.get())) {
                Toast.makeText(this@CreateAccountActivity , "Upload Success" , Toast.LENGTH_SHORT).show()
                finish()
            } else {
                val myAlert = MyAlert(this@CreateAccountActivity)
                myAlert.myDialog("Upload False" , "ไม่สามารถอัพโหลดข้อมูลดได้")
            }

        } catch (e : Exception) {
            e.printStackTrace()
        }

    }//upload
} //main