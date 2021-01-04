package com.lofrus.githubuserappv3.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonElement
import com.lofrus.githubuserappv3.BuildConfig
import com.lofrus.githubuserappv3.model.DetailUser
import com.lofrus.githubuserappv3.model.Repositories
import com.lofrus.githubuserappv3.model.User
import com.lofrus.githubuserappv3.retrofit.RetrofitClient
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel : ViewModel() {

    val detailUser = MutableLiveData<DetailUser>()
    val listFollowers = MutableLiveData<ArrayList<User>>()
    val listFollowing = MutableLiveData<ArrayList<User>>()
    val listRepositories = MutableLiveData<ArrayList<Repositories>>()
    var statusError = MutableLiveData<String?>()
    private val baseURL = "https://api.github.com/"
    private var authToken = BuildConfig.GithubAPIToken

    fun setDetailUser(username: String) {
        RetrofitClient(baseURL).instanceDetailUser.getDetailUser(authToken, username)
            .enqueue(
                object : Callback<JsonElement> {
                    override fun onResponse(
                        call: Call<JsonElement>,
                        response: Response<JsonElement>
                    ) {
                        if (response.isSuccessful) {
                            try {
                                val dataJSON = JSONObject(response.body().toString())
                                val userDetail = DetailUser()
                                userDetail.id = dataJSON.getInt("id")
                                userDetail.login = dataJSON.getString("login")
                                userDetail.avatar_url = dataJSON.getString("avatar_url")
                                userDetail.html_url = dataJSON.getString("html_url")
                                userDetail.name = dataJSON.getString("name")
                                userDetail.company = dataJSON.getString("company")
                                userDetail.blog = dataJSON.getString("blog")
                                userDetail.location = dataJSON.getString("location")
                                userDetail.email = dataJSON.getString("email")
                                userDetail.public_repos = dataJSON.getInt("public_repos")
                                userDetail.public_gists = dataJSON.getInt("public_gists")
                                userDetail.followers = dataJSON.getInt("followers")
                                userDetail.following = dataJSON.getInt("following")
                                userDetail.created_at = dataJSON.getString("created_at")
                                userDetail.updated_at = dataJSON.getString("updated_at")
                                detailUser.postValue(userDetail)
                            } catch (e: JSONException) {
                                Log.d("Exception (Detail)", e.message.toString())
                                statusError.value = "Exception (Detail) - " + e.message.toString()
                            }
                        } else {
                            Log.d("responseDetail.isFailed", "Response Not Successful")
                            statusError.value =
                                "responseDetail.isFailed - Response Not Successful$authToken"
                        }
                    }

                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        Log.d("onFailure (Detail)", t.message.toString())
                        statusError.value = "onFailure (Detail) - " + t.message.toString()
                    }
                })
    }

    fun setFollowerUser(username: String) {
        RetrofitClient(baseURL).instanceFollowers.getFollowers(authToken, username)
            .enqueue(
                object : Callback<JsonElement> {
                    override fun onResponse(
                        call: Call<JsonElement>,
                        response: Response<JsonElement>
                    ) {
                        if (response.isSuccessful) {
                            try {
                                val jsonArrayItem = JSONArray(response.body().toString())
                                val list = arrayListOf<User>()
                                for (i in 0 until jsonArrayItem.length()) {
                                    val dataJSON = jsonArrayItem.getJSONObject(i)
                                    val user = User()
                                    user.id = dataJSON.getInt("id")
                                    user.login = dataJSON.getString("login")
                                    user.avatar_url = dataJSON.getString("avatar_url")
                                    user.html_url = dataJSON.getString("html_url")
                                    list.add(user)
                                }
                                listFollowers.postValue(list)
                            } catch (e: JSONException) {
                                Log.d("Exception (Followers)", e.message.toString())
                                statusError.value =
                                    "Exception (Followers) - " + e.message.toString()
                            }
                        } else {
                            Log.d("responseFlwers.isFailed", "Response Not Successful")
                            statusError.value = "responseFlwers.isFailed - Response Not Successful"
                        }
                    }

                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        Log.d("onFailure (Followers)", t.message.toString())
                        statusError.value = "onFailure (Followers) - " + t.message.toString()
                    }
                })
    }

    fun setFollowingUser(username: String) {
        RetrofitClient(baseURL).instanceFollowing.getFollowing(authToken, username)
            .enqueue(
                object : Callback<JsonElement> {
                    override fun onResponse(
                        call: Call<JsonElement>,
                        response: Response<JsonElement>
                    ) {
                        if (response.isSuccessful) {
                            try {
                                val jsonArrayItem = JSONArray(response.body().toString())
                                val list = arrayListOf<User>()
                                for (i in 0 until jsonArrayItem.length()) {
                                    val dataJSON = jsonArrayItem.getJSONObject(i)
                                    val user = User()
                                    user.id = dataJSON.getInt("id")
                                    user.login = dataJSON.getString("login")
                                    user.avatar_url = dataJSON.getString("avatar_url")
                                    user.html_url = dataJSON.getString("html_url")
                                    list.add(user)
                                }
                                listFollowing.postValue(list)
                            } catch (e: JSONException) {
                                Log.d("Exception (Following)", e.message.toString())
                                statusError.value =
                                    "Exception (Following) - " + e.message.toString()
                            }
                        } else {
                            Log.d("responseFlwing.isFailed", "Response Not Successful")
                            statusError.value = "responseFlwing.isFailed - Response Not Successful"
                        }
                    }

                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        Log.d("onFailure (Following)", t.message.toString())
                        statusError.value = "onFailure (Following) - " + t.message.toString()
                    }
                })
    }

    fun setRepositoriesUser(username: String) {
        RetrofitClient(baseURL).instanceRepositories.getRepositories(authToken, username)
            .enqueue(
                object : Callback<JsonElement> {
                    override fun onResponse(
                        call: Call<JsonElement>,
                        response: Response<JsonElement>
                    ) {
                        if (response.isSuccessful) {
                            try {
                                val jsonArrayItem = JSONArray(response.body().toString())
                                val list = arrayListOf<Repositories>()
                                for (i in 0 until jsonArrayItem.length()) {
                                    val dataJSON = jsonArrayItem.getJSONObject(i)
                                    val repositories = Repositories()
                                    repositories.id = dataJSON.getInt("id")
                                    repositories.name = dataJSON.getString("name")
                                    repositories.html_url = dataJSON.getString("html_url")
                                    list.add(repositories)
                                }
                                listRepositories.postValue(list)
                            } catch (e: JSONException) {
                                Log.d("Exception (Repo)", e.message.toString())
                                statusError.value = "Exception (Repo) - " + e.message.toString()
                            }
                        } else {
                            Log.d("responseRepo.isFailed", "Response Not Successful")
                            statusError.value = "responseRepo.isFailed - Response Not Successful"
                        }
                    }

                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        Log.d("onFailure (Repo)", t.message.toString())
                        statusError.value = "onFailure (Repo) - " + t.message.toString()
                    }
                })
    }

    fun getDetailUser(): LiveData<DetailUser> {
        return detailUser
    }

    fun getFollowerUser(): LiveData<ArrayList<User>> {
        return listFollowers
    }

    fun getFollowingUser(): LiveData<ArrayList<User>> {
        return listFollowing
    }

    fun getRepositoriesUser(): LiveData<ArrayList<Repositories>> {
        return listRepositories
    }
}