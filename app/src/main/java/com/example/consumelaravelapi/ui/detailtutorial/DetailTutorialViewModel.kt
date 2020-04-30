package com.example.consumelaravelapi.ui.detailtutorial

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.consumelaravelapi.api.ApiHelper
import com.example.consumelaravelapi.api.RetrofitClient
import com.example.consumelaravelapi.entity.Comment
import com.example.consumelaravelapi.entity.Tutorial
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailTutorialViewModel : ViewModel() {
    private val tutorial = MutableLiveData<Tutorial>()
    private lateinit var token: String
    private lateinit var id: String
    private lateinit var onSuccessCallback: OnSuccessCallback

    private val apiHelper = RetrofitClient.getClient().create(ApiHelper::class.java)
    fun setSelectedTutorial(token: String, id: String) {
        this.token = token
        this.id = id
    }

    fun setOnSuccessCallBack(onSuccessCallback: OnSuccessCallback) {
        this.onSuccessCallback = onSuccessCallback
    }

    fun setData() {
        val tutorialComments = ArrayList<Comment>()
        val call = apiHelper.getDetailData(id)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("onFailure", t.message)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                val responseObject = JSONObject(response.body()?.string())
                val tutorialItem = Tutorial()
                tutorialItem.body = responseObject.getString("body")
                tutorialItem.title = responseObject.getString("title")
                tutorialItem.id = responseObject.getInt("id")
                tutorialItem.user_id = responseObject.getInt("user_id")

                val comments = responseObject.getJSONArray("comments")
                for (i in 0 until comments.length()) {
                    val commentItem = comments.getJSONObject(i)
                    val comment = Comment(
                        commentItem.getString("body"),
                        commentItem.getInt("id"),
                        commentItem.getInt("user_id"),
                        commentItem.getInt("tutorial_id")
                    )
                    tutorialComments.add(comment)
                    tutorialItem.comments = tutorialComments
                }
                tutorial.postValue(tutorialItem)

            }

        })
    }

    fun createComment(body: String) {
        val header = HashMap<String,String>()
        header["Authorization"] = token
        header["Content-Type"] = "application/json"
        header["Accept"] = "application/json"

        val comment = Comment()
        comment.body = body

        val call = apiHelper.createComment(header, id,comment)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onSuccessCallback.commentStatus("Komen Gagal ${t.message}")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful) {
                    onSuccessCallback.commentStatus("Komen Berhasil ${response.message()}")
                    Log.d("comment_berhasil" ,"Komen Berhasil ${response.body()?.string()}" )
                } else {
                    onSuccessCallback.commentStatus("Komen Gagal ${response.body()?.string()} ")
                }

                Log.d("token_komen","$token")
            }

        })
    }

    fun getData(): LiveData<Tutorial> = tutorial

    interface OnSuccessCallback {
        fun commentStatus(status: String)
    }
}