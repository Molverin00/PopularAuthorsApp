package com.example.popularauthorsapp.presentation.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
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

    inner class AuthorViewHolder(private val binding: ItemAuthorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val circularProgressDrawable = CircularProgressDrawable(itemView.context).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }

        fun bind(author: Author) {
            binding.tvAuthorName.text = author.name
            binding.tvPopularBookTitle.text = author.popularBookTitle
            binding.tvPublishedBooks.text =
                itemView.context.getString(R.string.number_books, author.numberPublishedBooks)
            // Load the author image using Glide or any other image loading library

            Glide.with(itemView)
                .load(author.image)
                .placeholder(circularProgressDrawable)
                .error(R.drawable.placeholder_author)
                .circleCrop()
                .into(binding.ivAuthorImage)

            // TODO: add url buttons for author and book
            binding.tvAuthorName.setOnClickListener {
                goToUrl(author.url)
            }

            binding.tvPopularBookTitle.setOnClickListener {
                goToUrl(author.popularBookUrl)
            }

        }

        private fun goToUrl(url: String) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                itemView.context.startActivity(intent)
            } catch (e: Exception) {
                println(e.localizedMessage)
            }
        }

    }

    private class AuthorDiffCallback(
        private val oldList: List<Author>,
        private val newList: List<Author>
    ) : DiffUtil.Callback() {
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