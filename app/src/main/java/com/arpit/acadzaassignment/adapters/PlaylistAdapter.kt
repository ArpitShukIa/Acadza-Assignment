package com.arpit.acadzaassignment.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arpit.acadzaassignment.Application
import com.arpit.acadzaassignment.ui.PlaylistActivity
import com.arpit.acadzaassignment.models.Playlist
import com.arpit.acadzaassignment.databinding.PlaylistItemLayoutBinding
import com.arpit.acadzaassignment.adapters.PlaylistAdapter.ViewHolder
import java.lang.Appendable

class PlaylistAdapter : ListAdapter<Playlist, ViewHolder>(PlaylistDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            PlaylistItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val playlist = getItem(position)
        holder.bind(playlist)
    }

    class ViewHolder(private val binding: PlaylistItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(playlist: Playlist) {
            binding.playlist = playlist
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                val intent = Intent(it.context, PlaylistActivity::class.java)
                if(playlist.videos == null)
                    intent.putExtra("playlistId", playlist.id)
                else {
                    intent.putExtra("playlistId", "")
                    (it.context.applicationContext as Application).playlist = playlist
                }
                it.context.startActivity(intent)
            }
        }
    }
}

class PlaylistDiffCallback : DiffUtil.ItemCallback<Playlist>() {
    override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return oldItem == newItem
    }
}