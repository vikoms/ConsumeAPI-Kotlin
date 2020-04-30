package com.example.consumelaravelapi.ui.detailtutorial

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumelaravelapi.R
import com.example.consumelaravelapi.entity.Comment
import com.example.consumelaravelapi.entity.Tutorial
import com.example.consumelaravelapi.ui.MainActivity
import com.example.consumelaravelapi.ui.detailtutorial.comment.CommentAdapter
import com.example.consumelaravelapi.ui.managetutorial.TutorialAddUpdateActivity
import kotlinx.android.synthetic.main.activity_detail_tutorial.*
import kotlinx.android.synthetic.main.dialog_new_comment.*

class DetailTutorialActivity : AppCompatActivity(), DetailTutorialViewModel.OnSuccessCallback {

    companion object {
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_USER_ID = "USER_ID"
    }
    private lateinit var viewModel: DetailTutorialViewModel
    private lateinit var userId: String
    private lateinit var tutorialId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tutorial)

        val extras = intent.extras
        if (extras != null) {
            tutorialId = extras.getInt(EXTRA_ID).toString()
            userId = extras.getInt(EXTRA_USER_ID).toString()
            val token = MainActivity.user.token
            if (tutorialId != null && token != null) {
                viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailTutorialViewModel::class.java]
                viewModel.setSelectedTutorial(token,tutorialId)
                populateView()

                fab_refresh.setOnClickListener {
                    populateView()
                }

                tv_new_comment.setOnClickListener{
                    initDialog()
                }
            }
        }

        if (supportActionBar != null) {
            supportActionBar?.title = "Detail Tutoriial"
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.update_tutorial_menu,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.update_tutorial) {
            val id = MainActivity.user.id.toString()
            if(userId.equals(id)) {
                val intent = Intent(this@DetailTutorialActivity, TutorialAddUpdateActivity::class.java).apply {
                    putExtra(TutorialAddUpdateActivity.TUTORIAL_ID,tutorialId)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Anda tidak bisa updata item ini", Toast.LENGTH_SHORT).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initDialog() {
        val dialog = Dialog(this@DetailTutorialActivity)
        with(dialog) {
            setContentView(R.layout.dialog_new_comment)
            getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        dialog.show()

        dialog.btn_new_comment.setOnClickListener {
            var newComment = dialog.edt_comment.text.toString()

            if (TextUtils.isEmpty(newComment)) {
                dialog.edt_comment.error = "Harap isi comment terlebih dahulu"
                dialog.edt_comment.setFocusable(true)
                return@setOnClickListener
            }

            viewModel.createComment(newComment)
            viewModel.setOnSuccessCallBack(this)
            dialog.dismiss()

        }
    }

    private fun populateView() {
        showLoading(true)

        viewModel.setData()
        viewModel.getData().observe(this, Observer { tutorial ->
            if(tutorial != null) {
                tv_tutorial_title.text = String.format(resources.getString(R.string.tutorial_title),tutorial.title)
                tv_tutorial_desc.text = tutorial.body
                populateComments(tutorial)
            }
        })
    }

    private fun populateComments(tutorial: Tutorial) {
        val commentAdapter =
            CommentAdapter()
        val comments = tutorial.comments
        if (comments != null) {
            commentAdapter.setData(comments)
            showLoading(false)
        }

        showLoading(false)
        with(rv_comments) {
            layoutManager = LinearLayoutManager(this@DetailTutorialActivity)
            adapter = commentAdapter
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            pg_tutorial.visibility = View.VISIBLE;
            rv_comments.visibility = View.INVISIBLE
        } else {
            pg_tutorial.visibility = View.INVISIBLE;
            rv_comments.visibility = View.VISIBLE
        }
    }

    override fun commentStatus(status: String) {
        Toast.makeText(this@DetailTutorialActivity, status,Toast.LENGTH_SHORT).show()
    }
}
