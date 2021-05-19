package com.diegoalarcon.emichat

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    //usar esta version de instanciar el autentication, para que Funcione la Respuesta del Log In

    private val mAuth: FirebaseAuth by lazy{FirebaseAuth.getInstance()}
    private val mGoogleSignInClient: GoogleSignInClient by lazy {getGoogleSignIn()}
    private val RC_GOOGLE_SIGN_IN = 99
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonLogIn.setOnClickListener{
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            if(isValidateEmail(email)&&isValidatePassword(password)){
                logInByEmail(email,password)
            }
            else{
                toast("Confirma que todos los datos introducidos son correctos")
            }
        }

        textViewForgotPassword.setOnClickListener{
            val goToForgotPassword = Intent(this,ForgotPasswordActivity::class.java)
            startActivity(goToForgotPassword)
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
        }

        buttonLogInGoogle.setOnClickListener{
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent,RC_GOOGLE_SIGN_IN)
        }

        buttonCreateAccountSignUp.setOnClickListener {
            val goToSignUp =Intent(this,SignUpActivity::class.java)
            startActivity(goToSignUp)
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
        }
        editTextEmail.validate{
            editTextEmail.error = if(isValidateEmail(it)) null else "El E-mail no es valido"
        }
        editTextPassword.validate {
            editTextPassword.error =
                if (isValidatePassword(it)) null else "El Password debe contener una minuscula, un numero, una mayuscula, y un caracter especial al menos"
        }
    }

    private fun loginByGoogleAccountIntoFirebase(googleAccount: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(googleAccount.idToken,null)
        mAuth.signInWithCredential(credential).addOnCompleteListener(this) {

                val goToMainActivity = Intent(this, MainActivity::class.java)
                startActivity(goToMainActivity)
                goToMainActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                val currentUser = mAuth.currentUser!!
                currentUser.displayName
                currentUser.email
                currentUser.photoUrl
                currentUser.phoneNumber
                currentUser.isEmailVerified
        }
    }

    private fun logInByEmail (email: String, password: String){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){task->
            if(task.isSuccessful){
                if(mAuth.currentUser!!.isEmailVerified) {
                    val goToMainActivity = Intent(this,MainActivity::class.java)
                    startActivity(goToMainActivity)
                    goToMainActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                    val currentUser = mAuth.currentUser!!
                    currentUser.displayName
                    currentUser.email
                    currentUser.photoUrl
                    currentUser.phoneNumber
                    currentUser.isEmailVerified
                } else {
                    toast("Debes confirmar tu Email primero")
                }
            }
            else{
                Toast.makeText(this,"Un Error inesperado ha ocurrido, por favor Intente de Nuevo",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_GOOGLE_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)

            if(result!!.isSuccess){
                val account = result.signInAccount
                loginByGoogleAccountIntoFirebase(account!!)
            }
        }
    }

    private fun getGoogleSignIn(): GoogleSignInClient{
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(this, gso)

    }

}
