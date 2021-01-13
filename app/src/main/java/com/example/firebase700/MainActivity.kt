package com.example.firebase700

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val analytics:FirebaseAnalytics= FirebaseAnalytics.getInstance(this)
        val bundle=Bundle()
        bundle.putString("message","Entró")
        analytics.logEvent("InitScreen",bundle)

        setup()
        sesion()

    }


    private fun  setup(){

        buttonRegistrar.setOnClickListener {
            if (editTextTextEmailAddress.text.isNotEmpty() && editTextTextPassword.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(editTextTextEmailAddress.text.toString(),
                editTextTextPassword.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        showOK()
                        persistencia()
                        sesion()
                    }
                    else {
                        showError()
                    }
                }
            }
        }


        buttonAcceder.setOnClickListener {
            if (editTextTextEmailAddress.text.isNotEmpty() && editTextTextPassword.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(editTextTextEmailAddress.text.toString(),
                    editTextTextPassword.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        showOK()
                        persistencia()
                        sesion()
                    }
                    else {
                        showError()
                    }
                }
            }
        }

        buttonSalir.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val prefs:SharedPreferences.Editor=getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()
            sesion()

        }



    }


    private fun sesion(){
        val prefs:SharedPreferences=getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE)
        val email=prefs.getString("email",null)
        if (email==null) {
            buttonSalir.isEnabled=false
            buttonAcceder.isEnabled=true
            buttonRegistrar.isEnabled=true
        }

        else
        {
            buttonSalir.isEnabled=true
            buttonAcceder.isEnabled=false
            buttonRegistrar.isEnabled=false
        }


    }
    private fun persistencia(){
        val prefs:SharedPreferences.Editor=getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit()
        prefs.putString("email",editTextTextEmailAddress.text.toString())
        prefs.apply()
    }

    private fun showOK() {
        val builder=AlertDialog.Builder(this)
        builder.setTitle("Ok")
        builder.setMessage("Conectado")
        builder.setPositiveButton("Aceptar",null)
        val dialog:AlertDialog=builder.create()
        dialog.show()
    }

    private fun showError() {
        val builder=AlertDialog.Builder(this)
        builder.setTitle("No")
        builder.setMessage("Error")
        builder.setPositiveButton("Aceptar",null)
        val dialog:AlertDialog=builder.create()
        dialog.show()
    }
}