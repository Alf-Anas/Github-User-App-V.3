package com.lofrus.githubuserappv3

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lofrus.githubuserappv3.databinding.ActivityMainBinding
import com.lofrus.githubuserappv3.model.ListUserAdapter
import com.lofrus.githubuserappv3.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ListUserAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.app_name_main)

        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter

        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        binding.svMain.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            val mHandler = Handler(Looper.getMainLooper())
            override fun onQueryTextChange(newText: String?): Boolean {
                mHandler.removeCallbacksAndMessages(null)
                mHandler.postDelayed({
                    if (!newText.isNullOrBlank()) {
                        showLoading(true)
                        mainViewModel.setListUsers(newText)
                    } else {
                        binding.llMainBG.visibility = View.VISIBLE
                        adapter.clearData()
                    }
                }, 850)
                return true
            }
        })

        mainViewModel.getListUsers().observe(this, { listUsers ->
            if (listUsers != null) {
                adapter.setData(listUsers)
                if (listUsers.isNotEmpty()) {
                    binding.llMainBG.visibility = View.GONE
                } else {
                    binding.llMainBG.visibility = View.VISIBLE
                }
            }
            showLoading(false)
        })

        mainViewModel.statusError.observe(this, { status ->
            if (status != null) {
                Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.app_bar_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite_ls -> startActivity(Intent(this, FavoriteActivity::class.java))
            R.id.setting_sh -> startActivity(Intent(this, SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarMain.visibility = View.VISIBLE
        } else {
            binding.progressBarMain.visibility = View.GONE
        }
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        binding.svMain.clearFocus()
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, R.string.double_back_pressed_to_exit, Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onResume() {
        super.onResume()
        binding.svMain.clearFocus()
    }

}
