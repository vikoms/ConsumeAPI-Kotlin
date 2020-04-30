package com.example.consumelaravelapi.ui.managetutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.consumelaravelapi.R
import com.example.consumelaravelapi.api.ApiHelper
import com.example.consumelaravelapi.api.RetrofitClient
import com.example.consumelaravelapi.ui.MainActivity
import com.example.consumelaravelapi.ui.detailtutorial.DetailTutorialViewModel
import com.example.consumelaravelapi.ui.tutorial.TutorialViewModel
import kotlinx.android.synthetic.main.activity_tutorial_add_update.*

class TutorialAddUpdateActivity : AppCompatActivity(), TutorialViewModel.TutorialInterface {

    private lateinit var apiHelper: ApiHelper
    private lateinit var viewModel: TutorialViewModel

    companion object {
        const val TUTORIAL_ID = "TUTORIAL_ID"
    }

    private lateinit var status: String
    private lateinit var tutorialId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_tutorial_add_update)
        showLoading(state = false)
        apiHelper = RetrofitClient.getClient().create(ApiHelper::class.java)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[TutorialViewModel::class.java]
        viewModel.setToken(MainActivity.user.token!!)
        status = "tambah"

        val extras = intent.extras
        if(extras != null) {
            tutorialId = extras.getString(TUTORIAL_ID,"")
            if (tutorialId != null) {
                showLoading(true)
                populateView(tutorialId)
                status = "edit"
            }
        }

        btn_save.setOnClickListener {
            showLoading(true)
            if(!status.equals("edit")) {
                saveData()
            } else {
                updateData()
            }
        }
    }

    private fun populateView(tutorialId: String) {
        val detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailTutorialViewModel::class.java]
        detailViewModel.setSelectedTutorial(MainActivity.user.token!!,tutorialId)
        detailViewModel.setData()
        detailViewModel.getData().observe(this, Observer { tutorial ->
            showLoading(false)
            edt_title_tutorial.setText(tutorial.title)
            edt_body_tutorial.setText(tutorial.body)
        })
    }

    private fun updateData() {
        var title = edt_title_tutorial.text.toString()
        var body = edt_body_tutorial.text.toString()
        val FIELD_REQUIRED = "FIELD REQUIRED"
        if(TextUtils.isEmpty(title)) {
            edt_title_tutorial.error = FIELD_REQUIRED
            edt_title_tutorial.setFocusable(true)
            return
        }
        if(TextUtils.isEmpty(body)) {
            edt_body_tutorial.error = FIELD_REQUIRED
            edt_body_tutorial.setFocusable(true)
            return
        }

        viewModel.updateData(title,body,tutorialId)
        viewModel.setTutorialIterface(this)
        showLoading(false)

    }

    private fun saveData() {
        var title = edt_title_tutorial.text.toString()
        var body = edt_body_tutorial.text.toString()
        val FIELD_REQUIRED = "FIELD REQUIRED"
        if(TextUtils.isEmpty(title)) {
            edt_title_tutorial.error = FIELD_REQUIRED
            edt_title_tutorial.setFocusable(true)
            return
        }
        if(TextUtils.isEmpty(body)) {
            edt_body_tutorial.error = FIELD_REQUIRED
            edt_body_tutorial.setFocusable(true)
            return
        }


        viewModel.createData(title,body)
        viewModel.setTutorialIterface(this)
        showLoading(false)
    }

    private fun showLoading (state: Boolean) {
        if(state) {
            btn_save.visibility = View.INVISIBLE
            pg_new_tutorial.visibility = View.VISIBLE
        } else {
            btn_save.visibility = View.VISIBLE
            pg_new_tutorial.visibility = View.INVISIBLE
        }
    }

    override fun addUpdateTutorial(status: String) {
        Toast.makeText(this@TutorialAddUpdateActivity, status , Toast.LENGTH_SHORT).show()
        finish()
    }
}
