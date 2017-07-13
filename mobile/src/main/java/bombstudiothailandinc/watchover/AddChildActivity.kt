package bombstudiothailandinc.watchover

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast

@SuppressLint("Registered")
class AddChildActivity : AppCompatActivity() {

    private var codeEditText : EditText? = null
    private var nameEditText : EditText? = null
    private var pictureImageView : ImageView? = null
    private var radioGroup : RadioGroup? = null
    private var codeString : String? = null
    private var nameString : String? = null
    private var genderString = "Male"
    private var loginStrings : Array<String>? = null


    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_child)

        //Get Value from Intent
        getValueFromIntent()


        //Initial view
        initialView()

        //RadioGroup Controller
        radioGroupController()

        //Back
        backController()

        //Camera Controller
        cameraController()

        //Save controller
        saveController()


    }  //Main Method

    private fun radioGroupController() {

        radioGroup !!.setOnCheckedChangeListener { radioGroup , i ->
            when (i) {
                R.id.radMale -> genderString = "Male"
                R.id.radFemale -> genderString = "Female"
            }
        }

    }

    private fun getValueFromIntent() {
        loginStrings = intent.getStringArrayExtra("Login")
    }

    private fun initialView() {
        pictureImageView = findViewById<View>(R.id.imPicture) as ImageView
        codeEditText = findViewById<View>(R.id.edtCode) as EditText
        nameEditText = findViewById<View>(R.id.edtName) as EditText
        radioGroup = findViewById<View>(R.id.ragGender) as RadioGroup

    }

    private fun saveController() {
        val imageView = findViewById<View>(R.id.imvSave) as ImageView
        imageView.setOnClickListener {
            //get value from edit text
            codeString = codeEditText !!.text.toString().trim { it <= ' ' }
            nameString = nameEditText !!.text.toString().trim { it <= ' ' }

            //check space
            if (codeString == "" || nameString == "") {
                //have space
                val myAlert = MyAlert(this@AddChildActivity)
                myAlert.myDialog("Have space" , "Please fill all")

            } else {
                //no space
                uploadValueToServer()

            }
        }
    }

    private fun uploadValueToServer() {


        val tag = "12JulyV1"
        Log.d(tag , "Code ==>" + codeString !!)
        Log.d(tag , "Name ==>" + nameString !!)
        Log.d(tag , "Gender ==>" + genderString)
        Log.d(tag , "ID_parent ==>" + loginStrings !![0])

        try {

            val postChildToServer = PostChildToServer(this@AddChildActivity)
            postChildToServer.execute(codeString , nameString , genderString , loginStrings !![0])
            val strResult = postChildToServer.get()
            Log.d(tag , "Result ==>" + strResult)

            if (java.lang.Boolean.parseBoolean(strResult)) {
                finish()
            } else {
                Toast.makeText(this@AddChildActivity , "Error Please Again" ,
                        Toast.LENGTH_SHORT).show()
            }

        } catch (e : Exception) {

            Log.d(tag , "e upload ==>" + e.toString())
        }
    }

    override fun onActivityResult(requestCode : Int , resultCode : Int , data : Intent) {
        super.onActivityResult(requestCode , resultCode , data)

        if (resultCode == Activity.RESULT_OK) {

            val uri = data.data
            try {

                val bitmap = BitmapFactory
                        .decodeStream(contentResolver.openInputStream(uri))
                pictureImageView !!.setImageBitmap(bitmap)

            } catch (e : Exception) {
                e.printStackTrace()
            }

        }

    } //onActivity

    private fun cameraController() {

        val cameraImageView = findViewById<View>(R.id.imvCamera) as ImageView

        cameraImageView.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent , 0)
        }
    }

    private fun backController() {
        val imageView = findViewById<View>(R.id.imvBack) as ImageView
        imageView.setOnClickListener { finish() }
    }
} //Main Class