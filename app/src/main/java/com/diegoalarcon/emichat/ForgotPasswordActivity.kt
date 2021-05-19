package com.diegoalarcon.emichat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy {FirebaseAuth.getInstance()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        editTextEmail.validate {
            editTextEmail.error = if(isValidateEmail(it)) null else "El Email no es valido"
        }

        buttonGoLogIn.setOnClickListener {
            val goToLoginActivity = Intent(this, LoginActivity::class.java)
            startActivity(goToLoginActivity)
            goToLoginActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }

        buttonReset.setOnClickListener {
            val email = editTextEmail.text.toString()
            if (isValidateEmail(email)){
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this){
                    toast("Un Email fue enviado para resetear tu password")
                    val goToLoginActivity = Intent(this,LoginActivity::class.java)
                    startActivity(goToLoginActivity)
                    goToLoginActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            } else{
                toast("Por Favor asegurate que la direccion de Email es correcta")
            }
        }
    }
}
