package com.androiddevs.mvvmnewsapp.features.fav

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.data.sharedPreferences.SharedPreferencesHelper
import com.androiddevs.mvvmnewsapp.features.shared.NewsAdapter
import com.androiddevs.mvvmnewsapp.databinding.FragmentSavedNewsBinding
import com.androiddevs.mvvmnewsapp.features.auth.signin.SignInActivity
import com.androiddevs.mvvmnewsapp.features.shared.NewsActivity
import com.androiddevs.mvvmnewsapp.features.shared.NewsViewModel
import com.androiddevs.mvvmnewsapp.utils.Constants
import com.google.android.material.snackbar.Snackbar
import com.tailors.doctoria.application.core.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.HashSet

@AndroidEntryPoint
class SavedNewsFragment : BaseFragment<FragmentSavedNewsBinding>(){
    lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        binding.appbar.appbarLogout.setOnClickListener {
            logout()
        }
        binding.appbar.appbarTxt.text = getString(R.string.saved)
        setUpRecyclerView()
        getSavedData()
        swipeToDelete()
        onItemClick()
        ItemTouchHelper(swipeToDelete()).attachToRecyclerView(binding.rvSavedNews)
    }
    private fun swipeToDelete() : ItemTouchHelper.SimpleCallback{
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.UP ,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
               return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                view?.let { Snackbar.make(it, "Successfully Deleted Article" , Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }
                    show()
                }

                }

            }

        }
        return itemTouchHelperCallback
    }

    private fun getSavedData(){
            val userId = SharedPreferencesHelper.getUser().id
            viewModel.getFavList(userId).observe(viewLifecycleOwner) {
                val favList = it?.favArticles ?: return@observe
                viewModel.getArticlesByIds(favList).observe(viewLifecycleOwner) {
                newsAdapter.differ.submitList(it)
            }
                }
    }

    private fun onItemClick() {
        newsAdapter.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.savedNewsFragment) {
                findNavController().navigate(
                    SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(it)
                )
            }
        }
    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            Log.d("test", "setUpRecyclerView")

        }
    }

}