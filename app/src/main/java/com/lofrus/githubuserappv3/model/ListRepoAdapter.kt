package com.lofrus.githubuserappv3.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lofrus.githubuserappv3.R
import com.lofrus.githubuserappv3.databinding.ItemListRepoBinding

class ListRepoAdapter : RecyclerView.Adapter<ListRepoAdapter.ListViewHolder>() {

    private val mData = ArrayList<Repositories>()
    fun setData(items: ArrayList<Repositories>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_list_repo, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListRepoBinding.bind(itemView)
        fun bind(repositories: Repositories) {
            binding.tvItemRepoName.text = repositories.name
            binding.tvItemRepoLink.text = repositories.html_url
        }
    }
}