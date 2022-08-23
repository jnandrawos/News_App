package com.example.newsapp.ui.home.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.ui.home.viewmodels.NewsViewModel
import com.example.newsapp.R
import com.example.newsapp.common.UtilityFunctions
import com.example.newsapp.ui.home.adapter.NewsAdapter
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.data.repository.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NewsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val newsViewModel: NewsViewModel by viewModels()
    private lateinit var binding: FragmentNewsBinding
    private lateinit var newsAdapter: NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentNewsBinding.inflate(layoutInflater, container, false)

        setupRecyclerView()
        implementListeners()
        initObservers()

        return binding.root
    }

    private fun implementListeners() {
        newsAdapter.setOnItemClickListener { article ->
            article.url?.let { goToArticle(it) }
        }
        binding.periodSpinner.onItemSelectedListener = this
    }

    private fun initObservers() {
        newsViewModel.mostViewedNews.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.results)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        UtilityFunctions.printLogs(getString(R.string.fetching_error), message)
                    }
                }
                is Resource.Loading ->
                    showProgressBar()
            }
        })
    }

    private fun hideProgressBar() {
        binding.pbLoading.visibility = View.INVISIBLE
        binding.rvNews.visibility = View.VISIBLE
        binding.tvNoEntries.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.pbLoading.visibility = View.VISIBLE
        binding.rvNews.visibility = View.INVISIBLE
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun goToArticle(url: String) {
        val action =
            NewsFragmentDirections.actionNewsFragmentToArticleFragment(
                url)
        findNavController().navigate(action)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> {
                newsViewModel.newsPeriod = 1
            }
            1 -> {
                newsViewModel.newsPeriod = 7
            }
            2 -> {
                newsViewModel.newsPeriod = 30
            }
            else -> {
                newsViewModel.newsPeriod = 1
            }
        }
        newsViewModel.getMostViewedNews()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}