package com.lofrus.githubuserappv3.fragment

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.lofrus.githubuserappv3.R

class DetailUserPagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return 3
    }

    lateinit var username: String
    private val tabTitles = intArrayOf(R.string.follower, R.string.following, R.string.repo)

    override fun getPageTitle(position: Int): CharSequence {
        return mContext.resources.getString(tabTitles[position])
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FolloweringFragment.newInstance(0, username)
            1 -> fragment = FolloweringFragment.newInstance(1, username)
            2 -> fragment = RepositoriesFragment.newInstance(username)
        }
        return fragment as Fragment
    }
}