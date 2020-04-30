package com.example.consumelaravelapi.ui.tutorial

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.consumelaravelapi.api.ApiHelper
import com.example.consumelaravelapi.api.RetrofitClient
import com.example.consumelaravelapi.entity.Tutorial
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TutorialViewModel : ViewModel() {
    private val listTutorial = MutableLiveData<ArrayList<Tutorial>>()
    private val apiHelper = RetrofitClient.getClient().create(ApiHelper::class.java)


    private lateinit var token: String
    private lateinit var tutorialInterface: TutorialInterface

    fun setTutorialIterface(tutorialInterface: TutorialInterface) {
        this.tutorialInterface = tutorialInterface
    }

    fun setToken(token: String) {
        this.token = token;
    }

    fun setData() {
        val tutorials = ArrayList<Tutorial>()
        val call = apiHelper.getData()
        call.enqueue(object : Callback<List<Tutorial>> {
            override fun onFailure(call: Call<List<Tutorial>>, t: Throwable) {
                Log.d("get_data_failed", "Get data filed ${t.message}")
            }

            override fun onResponse(
                call: Call<List<Tutorial>>,
                response: Response<List<Tutorial>>
            ) {
                if(response.isSuccessful) {
                    tutorials.addAll(response.body()!!)
                    listTutorial.postValue(tutorials)
                } else {
                    tutorialInterface.addUpdateTutorial("${response.message()}")
                }
            }

        })

    }

    fun createData(title: String, body: String) {
        val header = HashMap<String,String>()
        header["Authorization"] = token
        header["Content-Type"] = "application/json"
        header["Accept"] = "application/json"

        val tutorial = Tutorial()
        tutorial.title = title
        tutorial.body = body

        val call = apiHelper.createTutorial(header,tutorial)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                tutorialInterface.addUpdateTutorial("Add data failed ${t.message}")
                Log.d("cek_status", "Add data failed ${t.message}")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    tutorialInterface.addUpdateTutorial("Add data success ")
                    Log.d("add_tutorial_success","Add data success ${response.body()?.string()}")
                } else {
                    tutorialInterface.addUpdateTutorial("Add data failed ${response.message()}")
                    Log.d("cek_status","Add data failed ${response.message()}")
                }


            }

        })
    }

    fun deleteData(id: String) {
        val header = HashMap<String,String>()
        header["Authorization"] = token
        header["Content-Type"] = "application/json"
        header["Accept"] = "application/json"

        val call = apiHelper.deleteTutorial(header,id)
        call.enqueue(object: Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("delete_gagal_failure", "${t.message}")
                tutorialInterface.addUpdateTutorial("Delete tutorial gagal \n pesan : ${t.message}")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful) {
                    tutorialInterface.addUpdateTutorial("Delete tutorial berhasil")
                } else {
                    Log.d("delete_gagal", "${response.message()}")
                    tutorialInterface.addUpdateTutorial("Delete tutorial gagal \n pesan : ${response.message()}")
                }
            }

        })
    }

    fun updateData(title: String, body: String,id: String) {
        val header = HashMap<String,String>()
        header["Authorization"] = token
        header["Content-Type"] = "application/json"
        header["Accept"] = "application/json"

        val tutorial = Tutorial()
        tutorial.title = title
        tutorial.body = body

        val call = apiHelper.updateTutorial(header,tutorial,id)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                tutorialInterface.addUpdateTutorial("Update data failed ${t.message}")
                Log.d("cek_status_failure", "Update data failed ${t.message}")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    tutorialInterface.addUpdateTutorial("Update data success ")
                    Log.d("update_tutorial_success","Update data success ${response.body()?.string()}")
                } else {
                    tutorialInterface.addUpdateTutorial("Update data failed ${response.message()}")
                    Log.d("cek_status","Update data failed ${response.message()}")
                }


            }

        })
    }

    fun getData(): LiveData<ArrayList<Tutorial>> = listTutorial

    interface TutorialInterface {
        fun addUpdateTutorial(status: String)
    }
}