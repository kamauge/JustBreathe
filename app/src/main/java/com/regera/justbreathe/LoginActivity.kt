package com.regera.justbreathe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

private lateinit var auth: FirebaseAuth

private const val TAG = "LoginActivity"


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        logInBtn.setOnClickListener {

            logInUser()
        }
        clickHereBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }

    public override fun onStart() {
        super.onStart()
        checkLoggedState()
    }

    private fun logInUser(){

        var email = logInEmail.text.toString()
        val password = logInPassword.text.toString()


        if (email.isEmpty()){
            logInEmail.error = "Enter Email"
            logInEmail.requestFocus()


        }
        if (password.isEmpty()){
            logInPassword.error = "Enter Password"
            logInPassword.requestFocus()

        }

        if (email.isNotEmpty() && password.isNotEmpty()){
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        checkLoggedState()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        checkLoggedState()
                        // ...
                    }

                    // ...
                }
//            CoroutineScope(Dispatchers.IO).launch {
//                try {
//                    auth.signInWithEmailAndPassword(email,password).await()
//
//                    withContext(Dispatchers.Main){
//                        checkLoggedState()
//                    }
//
//                }
//                catch (e: Exception){
//                    withContext(Dispatchers.Main){
//
//                        Toast.makeText(this@LoginActivity,e.message, Toast.LENGTH_LONG).show()
//                    }
//                }
//            }
        }

    }

    private fun checkLoggedState() {

        if (auth.currentUser != null){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            Toast.makeText(this@LoginActivity,"You are logged in!",Toast.LENGTH_LONG).show()
            }
        else{
            Toast.makeText(this@LoginActivity,"You are not logged in yet!",Toast.LENGTH_LONG).show()
        }
    }
}