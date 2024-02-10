package com.udacity.asteroidradar.view

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidListItemBinding

class AsteroidAdapter(private val listener: OnClickListener) :
    ListAdapter<Asteroid, AsteroidAdapter.ViewHolder>(AsteroidDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.asteroid_list_item,
            parent,
            false
        )
    )

    class AsteroidDiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(
        private val binding: AsteroidListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid, listener : OnClickListener) {
            binding.model = asteroid
            binding.clickListener = OnClickListener { listener.onClick(asteroid) }
        }
    }

    interface OnClickListener {
        fun onClick(asteroid: Asteroid)
    }
}

