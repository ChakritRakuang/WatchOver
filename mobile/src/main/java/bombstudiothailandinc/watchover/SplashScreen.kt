package bombstudiothailandinc.watchover

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

@SuppressLint("Registered")
class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this@SplashScreen , HomeActivity::class.java))
            finish()
        } , 2000)
    }
}