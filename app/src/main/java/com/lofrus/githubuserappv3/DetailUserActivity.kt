package com.lofrus.githubuserappv3

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lofrus.githubuserappv3.databinding.ActivityDetailUserBinding
import com.lofrus.githubuserappv3.fragment.DetailUserPagerAdapter
import com.lofrus.githubuserappv3.model.User
import com.lofrus.githubuserappv3.room.UserFav
import com.lofrus.githubuserappv3.room.UserFavDatabase
import com.lofrus.githubuserappv3.viewmodel.DetailUserViewModel
import kotlin.concurrent.thread

class DetailUserActivity : AppCompatActivity() {

    private lateinit var detailUserViewModel: DetailUserViewModel
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var localDb: UserFavDatabase
    private var isFavorite: Boolean = false
    private lateinit var userDetail: UserFav

    companion object {
        const val DETAIL_USER = "detail_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        localDb = UserFavDatabase.getAppDatabase(this)!!

        detailUserViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailUserViewModel::class.java)

        val user = intent.getParcelableExtra<User>(DETAIL_USER) as User
        supportActionBar?.title =
            String.format(resources.getString(R.string.app_name_detail) + " @" + user.login)

        Glide.with(this)
            .load(user.avatar_url)
            .apply(
                RequestOptions()
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
            )
            .into(binding.imgDetailAvatar)
        binding.tvDetailUsername.text = user.login

        val detailUserPagerAdapter = DetailUserPagerAdapter(this, supportFragmentManager)
        detailUserPagerAdapter.username = user.login
        binding.viewPager.adapter = detailUserPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)

        getDetailUserData(user.login)

        binding.fabFavorite.setOnClickListener {
            thread {
                if (!isFavorite) {
                    localDb.usersFav().insert(userDetail)
                } else {
                    localDb.usersFav().deleteUserWithId(userDetail.id)
                }
                val thisUser = localDb.usersFav().getUserWithId(userDetail.id)
                if (thisUser != null) {
                    favoriteStatus(true)
                } else {
                    favoriteStatus(false)
                }
            }
        }
    }

    private fun getDetailUserData(login: String) {
        showLoading(true)
        detailUserViewModel.setDetailUser(login)

        detailUserViewModel.getDetailUser().observe(this, { detailUser ->
            if (detailUser != null) {
                binding.tvDetailName.text = checkNullOrEmptyString(detailUser.name)
                binding.tvDetailEmail.text = checkNullOrEmptyString(detailUser.email)
                binding.tvDetailLink.text = checkNullOrEmptyString(detailUser.html_url)
                binding.tvDetailLocation.text = checkNullOrEmptyString(detailUser.location)
                binding.tvDetailCompany.text = checkNullOrEmptyString(detailUser.company)

                binding.tabs.getTabAt(0)?.text =
                    detailUser.followers.toString() + "\n" + getString(R.string.follower)
                binding.tabs.getTabAt(1)?.text =
                    detailUser.following.toString() + "\n" + getString(R.string.following)
                binding.tabs.getTabAt(2)?.text =
                    detailUser.public_repos.toString() + "\n" + getString(R.string.repositories)

                userDetail = UserFav(
                    id = detailUser.id,
                    login = detailUser.login,
                    avatar_url = detailUser.avatar_url,
                    html_url = detailUser.html_url
                )

                thread {
                    val thisUser = localDb.usersFav().getUserWithId(detailUser.id)
                    if (thisUser != null) {
                        favoriteStatus(true)
                    }
                }
            }
            showLoading(false)
        })

        detailUserViewModel.statusError.observe(this, { status ->
            if (status != null) {
                Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun favoriteStatus(favorite: Boolean) {
        isFavorite = favorite
        if (favorite) {
            binding.fabFavorite.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_baseline_star_24)
            )
        } else {
            binding.fabFavorite.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_baseline_star_border_24)
            )
        }
    }

    private fun checkNullOrEmptyString(text: String?): String {
        return when {
            text.isNullOrBlank() -> {
                " - "
            }
            text == "null" -> {
                " - "
            }
            else -> {
                text
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarDetail.visibility = View.VISIBLE
        } else {
            binding.progressBarDetail.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

}