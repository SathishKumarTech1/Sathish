package com.task.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.task.myapplication.databinding.AdapterUsersBinding
import com.task.myapplication.models.gson.Data


class MoviePagerAdapter: PagingDataAdapter<Data, MoviePagerAdapter.MovieViewHolder>(MovieComparator) {
    var onItemClicked: ((Data, Int) -> Unit)? = null
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val data = getItem(position)!!
        holder.view.nameTV.text = data.first_name + " " + data.last_name
        holder.view.emailTV.text = data.email
        Glide.with(holder.itemView.context).load(data.avatar).into(holder.view.profileIV)

        holder.view.userCV.setOnClickListener {
            onItemClicked?.let {
                getItem(position)?.let { it ->
                    it(
                        it,
                        position
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterUsersBinding.inflate(inflater, parent, false)
        return MovieViewHolder(binding)
    }

    class MovieViewHolder(val view: AdapterUsersBinding): RecyclerView.ViewHolder(view.root) {

    }

    object MovieComparator: DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }
}

