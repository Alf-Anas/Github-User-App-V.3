package com.lofrus.githubuserappv3

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lofrus.githubuserappv3.model.ListUserAdapter
import com.lofrus.githubuserappv3.model.User
import com.lofrus.githubuserappv3.room.DBContract.UserFavColumns.Companion.CONTENT_URI
import com.lofrus.githubuserappv3.room.UserFavMappingHelper
import kotlin.concurrent.thread

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: ListUserAdapter
    private lateinit var llFavoriteBG: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        llFavoriteBG = findViewById(R.id.llFavoriteBG)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.app_name_favorite)

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}