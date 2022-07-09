package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.ArticleItemBinding
import com.example.newsapp.entities.Article

class NewsAdapter :
    RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {


    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }


    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.article_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            val binding: ArticleItemBinding = ArticleItemBinding.bind(holder.itemView)
            if (!article.media.isNullOrEmpty()) {
                Glide.with(holder.itemView).load(article.media[0].mediaMetadata[0].url)
                    .into(binding.ivArticleImage)
            } else {
                binding.ivArticleImage.visibility = View.GONE
            }
            binding.tvSource.text = article.source
            binding.tvTitle.text = article.title
            binding.tvDescription.text = article.abstract
            binding.tvPublishedAt.text = article.publishedDate
            holder.itemView.setOnClickListener {
                    onItemClickListener?.let {
                        it(article)
                    }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

}
