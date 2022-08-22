package com.example.newsapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.ArticleItemBinding
import com.example.newsapp.data.models.ArticleModel

class NewsAdapter :
    RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {


    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<ArticleModel>() {
        override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
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
            article.media.firstOrNull()?.mediaMetadata?.firstOrNull()?.let{
                Glide.with(holder.itemView).load(article.media[0].mediaMetadata[0].url)
                    .into(binding.ivArticleImage)
            } ?: run {
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


    private var onItemClickListener: ((ArticleModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (ArticleModel) -> Unit) {
        onItemClickListener = listener
    }

}
