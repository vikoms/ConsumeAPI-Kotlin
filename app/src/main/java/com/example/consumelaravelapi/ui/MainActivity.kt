package com.example.consumelaravelapi.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.consumelaravelapi.R
import com.example.consumelaravelapi.api.ApiHelper
import com.example.consumelaravelapi.api.RetrofitClient
import com.example.consumelaravelapi.entity.User
import com.example.consumelaravelapi.ui.tutorial.TutorialActivity
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    companion object {
        var user = User()
    }

    private lateinit var apiHelper: ApiHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiHelper = RetrofitClient.getClient().create(ApiHelper::class.java)
        btn_to_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))

        }

        btn_login.setOnClickListener {
            login()
        }

    }

    private fun login() {
        var username = edt_username_login.text.toString()
        var password = edt_password_login.text.toString()
//        val login = Login(username,password)
        val call = apiHelper.login(username, password)
        call.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("error_login", t.message)
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (!response.isSuccessful) {
                    Log.d("response_error", "${response.errorBody()} Code : ${response.code()}")
                    return
                }

                Toast.makeText(
                    this@MainActivity,
                    "Login Success ${response.body()?.id}",
                    Toast.LENGTH_SHORT
                ).show()
                user.id = response.body()?.id
                user.token = "Bearer ${response.body()?.token.toString()}"
                startActivity(Intent(this@MainActivity, TutorialActivity::class.java))
                finish()
            }

        })
    }

}
