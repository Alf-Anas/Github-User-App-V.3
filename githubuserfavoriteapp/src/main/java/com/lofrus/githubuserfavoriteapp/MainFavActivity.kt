package com.lofrus.githubuserfavoriteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lofrus.githubuserfavoriteapp.helper.DBContract.UserFavColumns.Companion.CONTENT_URI
import com.lofrus.githubuserfavoriteapp.helper.UserFavMappingHelper
import com.lofrus.githubuserfavoriteapp.model.ListUserAdapter
import com.lofrus.githubuserfavoriteapp.model.User
import kotlin.concurrent.thread

class MainFavActivity : AppCompatActivity() {

    private lateinit var adapter: ListUserAdapter
    private lateinit var llFavoriteBG: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_fav)

        llFavoriteBG = findViewById(R.id.llFavoriteBG)

        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        val rvFav: RecyclerView = findViewById(R.id.rvFavorite)
        rvFav.layoutManager = LinearLayoutManager(this)
        rvFav.adapter = adapter

        setData()
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    private fun setData() {
        thread {
            val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
            val listFavUsers = UserFavMappingHelper.mapCursorToArrayList(cursor)
            runOnUiThread {
                if (listFavUsers.isNotEmpty()) {
                    val list = arrayListOf<User>()
                    for (i in listFavUsers.indices) {
                        val userFav = listFavUsers[i]
                        val user = User()
                        user.id = userFav.id
                        user.login = userFav.login
                        user.avatar_url = userFav.avatar_url
                        user.html_url = userFav.html_url
                        list.add(user)
                    }
                    llFavoriteBG.visibility = View.GONE
                    adapter.setData(list)
                } else {
                    llFavoriteBG.visibility = View.VISIBLE
                    adapter.clearData()
                }
            }
        }
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, R.string.double_back_pressed_to_exit, Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}