package com.example.mynotesapplication

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    lateinit var btn_login_register:Button
    val AUTHUI_REQUEST_CODE = 111
    val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login_register = findViewById(R.id.button)

        if (FirebaseAuth.getInstance().currentUser != null){
            var intent: Intent = Intent(this,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()

        btn_login_register.setOnClickListener {
            performLogionAndRegister()
        }
    }

    private fun performLogionAndRegister() {
        var providerList : ArrayList<AuthUI.IdpConfig> = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        var intent:Intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providerList)
            .setLogo(R.mipmap.ic_launcher)
            .setTheme(R.style.GreenTheme)
            .build()

        startActivityForResult(intent,AUTHUI_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AUTHUI_REQUEST_CODE){
            if (requestCode == RESULT_OK){
                var user : FirebaseUser? = FirebaseAuth.getInstance().currentUser

                Log.d(TAG,"onActivityResult: "+ user!!.uid)

                if (user.metadata!!.creationTimestamp == user.metadata!!.lastSignInTimestamp){
                    Toast.makeText(this,"Welcome New User.",Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this,"Welcome Back User.",Toast.LENGTH_LONG).show()
                }

                var intent:Intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                this.finish()
            }else{
                var response: IdpResponse = IdpResponse.fromResultIntent(data)!!

                if (response == null){
                    Log.d(TAG,"onActivityResult: The user cancelled the sign in request.")
                }else{
                    Log.d(TAG,"onActivityResult: "+response.error)
                }
            }
        }
    }
}