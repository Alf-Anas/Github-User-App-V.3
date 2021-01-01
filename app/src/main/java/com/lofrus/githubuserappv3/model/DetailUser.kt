package com.lofrus.githubuserappv3.model

class DetailUser(
    var id: Int = 0,
    var login: String = "",
    var avatar_url: String = "",
    var html_url: String = "",
    var name: String? = "",
    var company: String? = "",
    var blog: String? = "",
    var location: String? = "",
    var email: String? = "",
    var public_repos: Int? = 0,
    var public_gists: Int? = 0,
    var followers: Int? = 0,
    var following: Int? = 0,
    var created_at: String? = "",
    var updated_at: String? = ""
)