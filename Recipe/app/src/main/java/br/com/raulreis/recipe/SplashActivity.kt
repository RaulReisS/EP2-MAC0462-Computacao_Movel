package br.com.raulreis.recipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Somente obtendo o token para enviar uma push
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d(TAG, token.toString())
        })
        //duBYiGAuRUuYrZMZpIFqyP:APA91bGmNtsB2JAq-cWZvHtczR3aCbCQM2f28HfiyKG5y2bU5jnJ5IXFqg43VRWi1xkDEQBmKi85Au3Vz_6UgDBPDaenKcQRbRty3yvSX62DPwz3ubuVLCpvdS-VT5adYl-Lql8IyiuW


        // Manipualação da SplashScreen
        Handler(Looper.getMainLooper()).postDelayed({
        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
                                                    },2500)
    }

    companion object {
        private const val TAG = "SplashActivity"
    }
}