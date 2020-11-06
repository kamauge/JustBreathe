package com.regera.justbreathe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

private lateinit var auth: FirebaseAuth

private lateinit var email:String
private lateinit var password:String


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         auth = FirebaseAuth.getInstance()


        regBtn.setOnClickListener {
            registerUser()
        }


    }

    private fun registerUser(){
        email = regEmail.text.toString()
        password = regPassword.text.toString()

        if (email.isEmpty()){
            regEmail.error = "Enter Email"
            regEmail.requestFocus()


        }
        if (password.isEmpty()){
            regEmail.error = "Enter Password"
            regEmail.requestFocus()

        }

        if (email.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email,password).await()
                    withContext(Dispatchers.Main){

                        user()
                    }

                }
                catch (e: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }

    private fun user(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }



}


