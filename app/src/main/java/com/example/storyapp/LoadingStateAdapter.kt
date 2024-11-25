package com.example.storyapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView

class LoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingStateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_loading, parent, false)
        return LoadingStateViewHolder(view, retry)
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class LoadingStateViewHolder(itemView: View, private val retry: () -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        private val tvError: TextView = itemView.findViewById(R.id.tvError)

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Loading) {
                progressBar.visibility = View.VISIBLE
                tvError.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
                if (loadState is LoadState.Error) {
                    tvError.visibility = View.VISIBLE
                    tvError.setOnClickListener { retry.invoke() }
                }
            }
        }
    }
}