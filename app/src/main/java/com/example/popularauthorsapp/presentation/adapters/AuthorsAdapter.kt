package com.example.popularauthorsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.popularauthorsapp.R
import com.example.popularauthorsapp.data.model.Author
import com.example.popularauthorsapp.databinding.ItemAuthorBinding

class AuthorsAdapter : RecyclerView.Adapter<AuthorsAdapter.AuthorViewHolder>() {

    private val authorsList = mutableListOf<Author>()

    fun submitList(newList: List<Author>) {
        val diffCallback = AuthorDiffCallback(authorsList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        authorsList.clear()
        authorsList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorViewHolder {
        val binding = ItemAuthorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AuthorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AuthorViewHolder, position: Int) {
        val currentAuthor = authorsList[position]
        holder.bind(currentAuthor)
    }

    override fun getItemCount(): Int = authorsList.size

    inner class AuthorViewHolder(private val binding: ItemAuthorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(author: Author) {
            binding.tvAuthorName.text = author.name
            binding.tvPopularBookTitle.text = author.popularBookTitle
            binding.tvPublishedBooks.text = author.numberPublishedBooks.toString()
            // Load the author image using Glide or any other image loading library
            Glide.with(itemView)
                .load(author.image)
                .placeholder(R.drawable.placeholder_author)
                .into(binding.ivAuthorImage)

            // TODO: add url buttons for author and book
        }
    }

    private class AuthorDiffCallback(private val oldList: List<Author>, private val newList: List<Author>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].authorId == newList[newItemPosition].authorId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}