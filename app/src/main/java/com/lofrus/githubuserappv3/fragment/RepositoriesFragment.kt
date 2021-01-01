package com.lofrus.githubuserappv3.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lofrus.githubuserappv3.databinding.FragmentRepositoriesBinding
import com.lofrus.githubuserappv3.model.ListRepoAdapter
import com.lofrus.githubuserappv3.viewmodel.DetailUserViewModel

class RepositoriesFragment : Fragment() {

    private lateinit var adapter: ListRepoAdapter
    private lateinit var detailUserViewModel: DetailUserViewModel
    private lateinit var binding: FragmentRepositoriesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListRepoAdapter()
        adapter.notifyDataSetChanged()

        binding.rvRepositories.layoutManager = LinearLayoutManager(activity)
        binding.rvRepositories.adapter = adapter

        detailUserViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailUserViewModel::class.java)

        val username = arguments?.getString(ARG_USERNAME, "")

        username?.let { detailUserViewModel.setRepositoriesUser(it) }
        activity?.let {
            detailUserViewModel.getRepositoriesUser().observe(it, { listRepositories ->
                if (listRepositories != null) {
                    adapter.setData(listRepositories)
                }
            })
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

        binding = FragmentRepositoriesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    companion object {
        private const val ARG_USERNAME = "username"

        @JvmStatic
        fun newInstance(login: String) =
            RepositoriesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME, login)
                }
            }
    }
}