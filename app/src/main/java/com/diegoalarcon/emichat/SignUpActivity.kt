package com.diegoalarcon.emichat

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    //paso 3 de firebase, declara instancia firebaseauth
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

//Devuelve al Activity Login
        buttonGoLogIn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }


//obtiene el email y el password de los ediText, y valida si "isValidEmailAndPassword" y sus variables introducidas llego a TRUE
        //entonces ejecuta signUpByEmail introduciendo sus variables
        buttonSignUp.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val confirmPassword = editTextConfirmPassword.text.toString()
            if (isValidateEmail(email) && isValidatePassword(password) && isValidateConfirmPassword(password, confirmPassword)) {
                signUpByEmail(email, password)
            } else {
                toast("Confirma que todos los datos introducidos son correctos")
            }

        }
//verifica si son validos los editText introducidos en el SignUp, estos se hallan creados en el archivos "extensions.kt",
// y son llamados a esta activity, e implementados a traves de los siguientes metodos
        //son 2 metodos creados en extensions, implementados aqui, validate y isvalid"Email"Password"ConfirmPassword
        //son para para evitar hacer "Boulderplate"que es generar mucho codigo extenso y repetitivo
        editTextEmail.validate {
            editTextEmail.error = if (isValidateEmail(it)) null else "El E-mail no es valido"
        }
        editTextPassword.validate {
            editTextPassword.error =
                if (isValidatePassword(it)) null else "El Password debe contener una minuscula, un numero, una mayuscula, y un caracter especial al menos"
        }
        editTextConfirmPassword.validate {
            editTextConfirmPassword.error = if (isValidateConfirmPassword(
                    editTextPassword.text.toString(),
                    it
                )
            ) null else "La confirmacion del Password no coincide con el password "
        }
    }

    //Obtiene los datos los editext Email y Password, a traves de la accion del buttonSignUp (si es que son Validos), y ademas envia un correo de confirmacion
    private fun signUpByEmail(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                mAuth.currentUser!!.sendEmailVerification().addOnCompleteListener(this) {
                    toast("Se te ha enviado un Email, Por favor confirma, antes de continuar")

                    val goToLoginActivity = Intent(this, LoginActivity::class.java)
                    goToLoginActivity.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(goToLoginActivity)

                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
                // Sign in success, update UI with the signed-in user's information

            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(
                    this,
                    "Ha ocurrido un error inesperado. por favor intenta otra vez",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}





