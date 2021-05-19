package com.diegoalarcon.emichat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

//login and main activity Flow
class MainEmptyActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//verificar si el usuario esta conectado, o ha logeado previamente, si lo hizo va directamente al main activity, si no lo esta va al login
        if(mAuth.currentUser==null) {
            val goToLoginActivity = Intent(this, LoginActivity::class.java)
            goToLoginActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(goToLoginActivity)

        } else{
            val goToMainActivity = Intent(this, MainActivity::class.java)
            goToMainActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(goToMainActivity)

        }
        finish()
    }
}