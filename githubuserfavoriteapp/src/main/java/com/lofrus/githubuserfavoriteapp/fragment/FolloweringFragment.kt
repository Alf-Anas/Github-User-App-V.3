package com.lofrus.githubuserfavoriteapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lofrus.githubuserfavoriteapp.databinding.FragmentFolloweringBinding
import com.lofrus.githubuserfavoriteapp.model.ListUserAdapter
import com.lofrus.githubuserfavoriteapp.viewmodel.DetailUserViewModel

class FolloweringFragment : Fragment() {

    private lateinit var adapter: ListUserAdapter
    private lateinit var detailUserViewModel: DetailUserViewModel
    private lateinit var binding: FragmentFolloweringBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        binding.rvFollowering.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowering.adapter = adapter

        detailUserViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailUserViewModel::class.java)

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = arguments?.getString(ARG_USERNAME, "")

        if (index == 0) {
            username?.let { detailUserViewModel.setFollowerUser(it) }
            activity?.let {
                detailUserViewModel.getFollowerUser().observe(it, { listFollowers ->
                    if (listFollowers != null) {
                        adapter.setData(listFollowers)
                    }
                })
            }
        } else if (index == 1) {
            username?.let { detailUserViewModel.setFollowingUser(it) }
            activity?.let {
                detailUserViewModel.getFollowingUser().observe(it, { listFollowing ->
                    if (listFollowing != null) {
                        adapter.setData(listFollowing)
                    }
                })
            }
        }

        activity?.let {
            detailUserViewModel.statusError.observe(it, { status ->
                if (status != null) {
                    Toast.makeText(activity, status, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFolloweringBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val ARG_USERNAME = "username"

        @JvmStatic
        fun newInstance(index: Int, login: String) =
            FolloweringFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                    putString(ARG_USERNAME, login)
                }
            }
    }
}