package com.arpit.acadzaassignment.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arpit.acadzaassignment.models.Playlist
import com.arpit.acadzaassignment.adapters.PlaylistAdapter
import com.bumptech.glide.Glide

@BindingAdapter("playlists")
fun setItems(listView: RecyclerView, items: List<Playlist>?) {
    items?.let {
        (listView.adapter as PlaylistAdapter).submitList(items)
        listView.smoothScrollToPosition(0)
    }
}

@BindingAdapter("thumbnail")
fun setThumbnail(imageView: ImageView, url: String) {
    Glide.with(imageView.context)
        .load(url)
        .into(imageView)
}

@BindingAdapter("visibility")
fun setVisibility(textView: TextView, status: String) {
    if(status.isEmpty())
        textView.visibility = View.GONE
}