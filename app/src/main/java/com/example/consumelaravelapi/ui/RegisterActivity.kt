package com.example.consumelaravelapi.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.consumelaravelapi.R
import com.example.consumelaravelapi.api.ApiHelper
import com.example.consumelaravelapi.api.RetrofitClient
import com.example.consumelaravelapi.entity.Register
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.edt_password_login
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var apiHelper: ApiHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        apiHelper = RetrofitClient.getClient().create(ApiHelper::class.java)
        btn_register.setOnClickListener {
            register();
        }

        btn_to_login.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun register() {
        var email = tv_email.text.toString()
        var username = tv_username.text.toString()
        var password = edt_password_login.text.toString()

        val register = Register(username,email,password)

        val call = apiHelper.register(register)
        call.enqueue(object: Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("error_register1",t.message)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "register success",Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("error_register2","${response.code()}")
                }
            }

        })


    }
}
