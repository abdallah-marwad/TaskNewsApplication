package com.androiddevs.mvvmnewsapp.features.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.data.models.article.NewsResponse
import com.androiddevs.mvvmnewsapp.features.shared.NewsAdapter
import com.androiddevs.mvvmnewsapp.databinding.FragmentBreakingNewsBinding
import com.androiddevs.mvvmnewsapp.features.shared.NewsActivity
import com.androiddevs.mvvmnewsapp.features.shared.NewsViewModel
import com.androiddevs.mvvmnewsapp.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.androiddevs.mvvmnewsapp.utils.Resource
import com.tailors.doctoria.application.core.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreakingNewsFragment : BaseFragment<FragmentBreakingNewsBinding>() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsTopAdapter: TopHeadLineAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setUpRecyclerViewNews()
        setUpRecyclerViewTopHeadLines()
        viewModelObserving()
        viewModelObservingTopNews()
        onItemClick()
        topHeadLineOnItemClick()
    }

    private fun onItemClick() {
        newsAdapter.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.breakingNewsFragment) {
                findNavController().navigate(
                    BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(it)
                )
            }
        }
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true

    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun viewModelObserving() {
        viewModel.breakingNews.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    hideProgressBar()
                    it.message?.let { message -> showDialogWithMsg(message)} }
                is Resource.Loading -> showProgressBar()
                is Resource.Success -> {
                  handleSuccessGetNews(it)
                }
            }
        }
    }
    private fun viewModelObservingTopNews() {
        viewModel.topBreakingNews.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    hideProgressBar()
                    it.message?.let { message -> showDialogWithMsg(message)} }
                is Resource.Loading -> showProgressBar()
                is Resource.Success -> {
                  handleSuccessGetTopNews(it)
                }
            }
        }
    }

    private fun handleSuccessGetNews(reponse: Resource.Success<NewsResponse>) {
        hideProgressBar()
        reponse.data?.let { newsResponse ->
            binding.headlinesTxt.visibility = View.VISIBLE
            newsAdapter.differ.submitList(newsResponse.articles.toList())
            val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
            isLastPage = viewModel.breakingNewsPage == totalPages
            if (isLastPage) {
                binding.rvBreakingNews.setPadding(0, 0, 0, 0)
            }
        }
    }
    private fun handleSuccessGetTopNews(reponse: Resource.Success<NewsResponse>) {
        hideProgressBar()
        reponse.data?.let { newsResponse ->
            newsTopAdapter.differ.submitList(newsResponse.articles.toList())
            binding.allNewsTxt.visibility = View.VISIBLE
        }
    }


    var isLoading = false
    var isScrolling = false
    var isLastPage = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            // error could be in  and
            val isNotLoadingAndNotLastPage = !isLoading and !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage and isAtLastItem and isNotAtBeginning and isTotalMoreThanVisible and isScrolling
            if (shouldPaginate) {
                viewModel.getBreakingNews()
                isScrolling = false
            }

        }
    }

    private fun setUpRecyclerViewNews() {
        newsAdapter = NewsAdapter()
        newsAdapter.mListCount = 20
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
//            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
            Log.d("test", "setUpRecyclerViewNews")
        }
    }
    private fun setUpRecyclerViewTopHeadLines() {
        newsTopAdapter = TopHeadLineAdapter()
        binding.rvTopBreakingNews.apply {
            adapter = newsTopAdapter
        }
    }
    private fun topHeadLineOnItemClick() {
        newsTopAdapter.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.breakingNewsFragment) {
                findNavController().navigate(
                    BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(it)
                )
            }
        }
    }
}