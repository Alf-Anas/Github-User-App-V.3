package com.lofrus.githubuserfavoriteapp

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lofrus.githubuserfavoriteapp.databinding.ActivityDetailUserBinding
import com.lofrus.githubuserfavoriteapp.fragment.DetailUserPagerAdapter
import com.lofrus.githubuserfavoriteapp.helper.DBContract.UserFavColumns.Companion.CONTENT_URI
import com.lofrus.githubuserfavoriteapp.model.User
import com.lofrus.githubuserfavoriteapp.model.UserFav
import com.lofrus.githubuserfavoriteapp.model.UserFav.Companion.COLUMN_AVATAR_URL
import com.lofrus.githubuserfavoriteapp.model.UserFav.Companion.COLUMN_HTML_URL
import com.lofrus.githubuserfavoriteapp.model.UserFav.Companion.COLUMN_ID
import com.lofrus.githubuserfavoriteapp.model.UserFav.Companion.COLUMN_LOGIN
import com.lofrus.githubuserfavoriteapp.viewmodel.DetailUserViewModel
import kotlin.concurrent.thread

class DetailUserActivity : AppCompatActivity() {

    private lateinit var detailUserViewModel: DetailUserViewModel
    private lateinit var binding: ActivityDetailUserBinding
    private var isFavorite: Boolean = false
    private lateinit var userDetail: UserFav
    private lateinit var uriWithId: Uri

    companion object {
        const val DETAIL_USER = "detail_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
                    val values = ContentValues()
                    values.put(COLUMN_ID, userDetail.id)
                    values.put(COLUMN_LOGIN, userDetail.login)
                    values.put(COLUMN_AVATAR_URL, userDetail.avatar_url)
                    values.put(COLUMN_HTML_URL, userDetail.html_url)
                    contentResolver.insert(uriWithId, values)
                } else {
                    contentResolver.delete(uriWithId, null, null)
                }
                val cursor = contentResolver.query(uriWithId, null, null, null, null)
                if (cursor != null) {
                    if (cursor.count > 0) {
                        favoriteStatus(true)
                    } else {
                        favoriteStatus(false)
                    }
                    cursor.close()
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
                    uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + detailUser.id)
                    val cursor = contentResolver.query(uriWithId, null, null, null, null)
                    if (cursor != null) {
                        if (cursor.count > 0) {
                            favoriteStatus(true)
                        }
                        cursor.close()
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