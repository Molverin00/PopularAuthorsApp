package com.example.popularauthorsapp.ui.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.popularauthorsapp.R
import com.example.popularauthorsapp.data.model.Author

class AuthorAdapter(private var authors: List<Author>) :
    RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_author, parent, false)
        return AuthorViewHolder(view)
    }

    override fun onBindViewHolder(holder: AuthorViewHolder, position: Int) {
        val author = authors[position]
        holder.bind(author)
    }

    override fun getItemCount(): Int {
        return authors.size
    }

    fun setData(newData: List<Author>) {
        authors = newData
        notifyDataSetChanged()
    }

    inner class AuthorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(author: Author) {
            // Bind the author data to the views in the item_author layout
            itemView.findViewById<TextView>(R.id.tv_authorName).text = author.name
            itemView.findViewById<TextView>(R.id.tv_popularBookTitle).text = author.popularBookTitle
            itemView.findViewById<TextView>(R.id.tv_publishedBooks).text =
                "author of  ${author.numberPublishedBooks} Books"

            // Fetch author picture from image url
            Glide.with(itemView.context)
                .load(author.image)
                .centerInside()
                .circleCrop()
                .placeholder(R.drawable.placeholder_author)
                .error(R.drawable.placeholder_author)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(itemView.findViewById(R.id.iv_authorPicture))

            // Fetch book cover from book url
            Glide.with(itemView.context)
                .load(author.popularBookUrl)
                .centerInside()
                .placeholder(R.drawable.placeholder_book)
                .error(R.drawable.placeholder_book)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(itemView.findViewById(R.id.iv_authorPicture))

            // Set intent for author page url
            itemView.findViewById<Button>(R.id.btn_author_page).setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(author.url))
                itemView.context.startActivity(intent)
            }

            // Set intent for book page url
            itemView.findViewById<Button>(R.id.btn_book_page).setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(author.popularBookUrl))
                itemView.context.startActivity(intent)
            }
        }
    }
}