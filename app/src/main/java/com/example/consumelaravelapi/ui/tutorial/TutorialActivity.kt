package com.example.consumelaravelapi.ui.tutorial

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumelaravelapi.R
import com.example.consumelaravelapi.api.ApiHelper
import com.example.consumelaravelapi.api.RetrofitClient
import com.example.consumelaravelapi.entity.Tutorial
import com.example.consumelaravelapi.ui.MainActivity
import com.example.consumelaravelapi.ui.detailtutorial.DetailTutorialActivity
import com.example.consumelaravelapi.ui.managetutorial.TutorialAddUpdateActivity
import kotlinx.android.synthetic.main.activity_tutorial.*

class TutorialActivity : AppCompatActivity(), TutorialAdapter.OnItemClickCallback,
    TutorialViewModel.TutorialInterface {

    private lateinit var apiHelper: ApiHelper
    private lateinit var viewModel: TutorialViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
        apiHelper = RetrofitClient.getClient().create(ApiHelper::class.java)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[TutorialViewModel::class.java]
        viewModel.setToken(MainActivity.user.token!!)
        populateTutorial();
        fab_refresh.setOnClickListener {
            populateTutorial()
        }

        if (supportActionBar != null) {
            supportActionBar?.title = "Tutorial List"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.manage_tutorial_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.manage_tutorial) {
            startActivity(Intent(this@TutorialActivity, TutorialAddUpdateActivity::class.java))
        }
        return true
    }

    private fun populateTutorial() {
        showLoading(true)
        val tutorialAdapter = TutorialAdapter(this)
        viewModel.setData()
        viewModel.getData().observe(this, Observer { tutorials ->
            if (tutorials != null) {
                showLoading(false)
                tutorialAdapter.setData(tutorials)
            }
        })
        viewModel.setTutorialIterface(this)
        with(rv_tutorial) {
            layoutManager = LinearLayoutManager(this@TutorialActivity)
            adapter = tutorialAdapter
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            pg_tutorial.visibility = View.VISIBLE;
            rv_tutorial.visibility = View.INVISIBLE;
        } else {
            pg_tutorial.visibility = View.INVISIBLE;
            rv_tutorial.visibility = View.VISIBLE;
        }
    }

    override fun onItemClicked(data: Tutorial, action: String) {

        when(action) {
            "show" -> {
                val intent = Intent(this@TutorialActivity, DetailTutorialActivity::class.java).apply {
                    putExtra(DetailTutorialActivity.EXTRA_ID, data.id)
                    putExtra(DetailTutorialActivity.EXTRA_USER_ID,data.user_id)
                }

                startActivity(intent)
            }
            "delete" -> showDialog(data)
        }

    }

    private fun showDialog(data: Tutorial) {
        val dialog = AlertDialog.Builder(this@TutorialActivity)
        dialog.setMessage("anda yakin akan menghapus data ini")
        dialog.setMessage("Hapus Tutorial")

        dialog.setPositiveButton("Yes", DialogInterface.OnClickListener{ dialog,id ->
            viewModel.deleteData(data.id.toString())
        }).setNegativeButton("No", DialogInterface.OnClickListener{dialog,id ->
            dialog.cancel()
        })

        val builder = dialog.create()
        builder.show()
    }

    override fun addUpdateTutorial(status: String) {
        Toast.makeText(this@TutorialActivity, status, Toast.LENGTH_SHORT).show()

    }

}
