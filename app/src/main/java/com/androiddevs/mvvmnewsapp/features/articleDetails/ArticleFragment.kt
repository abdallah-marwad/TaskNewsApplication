package com.androiddevs.mvvmnewsapp.features.articleDetails

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.data.sharedPreferences.SharedPreferencesHelper
import com.androiddevs.mvvmnewsapp.databinding.FragmentArticleBinding
import com.androiddevs.mvvmnewsapp.features.shared.NewsActivity
import com.androiddevs.mvvmnewsapp.features.shared.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.HashSet

@AndroidEntryPoint
class ArticleFragment : Fragment(R.layout.fragment_article) {
    lateinit var viewModel: NewsViewModel
    lateinit var binding: FragmentArticleBinding
    val args by navArgs<ArticleFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)
        viewModel = (activity as NewsActivity).viewModel
        showWebView()
        saveArticle()

    }
    // SHow Article In WebView

    private fun showWebView() {
        val article = args.articleParcelable
        binding.webView.apply {
            webViewClient = WebViewClient()
            article?.url?.let { loadUrl(it) }
        }
    }


    //add article to room

    private fun saveArticle() {
        binding.fab.setOnClickListener { view ->
            lifecycleScope.launchWhenStarted {
            val userId = SharedPreferencesHelper.getUser().id
            viewModel.saveArticle(args.articleParcelable)
            viewModel.addedArticle.collect { savedId ->
                viewModel.getFavList(userId).observe(viewLifecycleOwner) {
                    val favList = it?.favArticles?.toHashSet() ?: HashSet()
                    favList.add(savedId.toInt())
                    viewModel.updateFavArticles(userId, favList.toList())
                }
                saveItemCallback()
            }
        }
            }
        }

    private fun saveItemCallback() {
        lifecycleScope.launchWhenStarted {
            viewModel.addStateFlow.collect {
                Snackbar.make(binding.webView, "Successfully Saved ", Snackbar.LENGTH_SHORT)
                    .show()

            }
        }
    }
}

