package com.skorik05.autodoctest.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.skorik05.autodoctest.R
import com.skorik05.autodoctest.databinding.FragmentRepositoriesBinding
import com.skorik05.autodoctest.domain.SharedViewModel
import com.skorik05.autodoctest.model.dagger.DaggerMainComponent
import com.skorik05.autodoctest.model.items.RepositoryItem
import com.skorik05.autodoctest.presentation.adapters.RepositoryAdapter

class RepositoriesFragment: Fragment() {

    private lateinit var binding: FragmentRepositoriesBinding
    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter: RepositoryAdapter
    private val daggerMainComponent = DaggerMainComponent.builder().build()
    private val picasso = daggerMainComponent.getPicasso()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRepositoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            adapter = RepositoryAdapter(it, picasso)
        }
        binding.rvRepositories.adapter = adapter
        binding.etSearch.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.loadRepositories(v.text.toString(), 1)
                v.clearFocus()
                v.hideKeyboard()
                true
            } else {
                false
            }
        }
        binding.clSearch.setOnClickListener { binding.rvRepositories.smoothScrollToPosition(0) }
        binding.srlRepositories.setOnRefreshListener { viewModel.loadRepositories(binding.etSearch.text.toString()) }
        binding.bFirstPage.bPage.text = "<"
        binding.bLastPage.bPage.text = ">"
        viewModel.repositories.observe(viewLifecycleOwner, ::updateRepositories)
        viewModel.firstPageNum.observe(viewLifecycleOwner, ::setFirstPageButton)
        viewModel.lastPageNum.observe(viewLifecycleOwner, ::setLastPageButton)
        viewModel.currentPagePosition.observe(viewLifecycleOwner, ::setCurrentPagePosition)
        viewModel.availablePages.observe(viewLifecycleOwner, ::setAvailablePages)
        viewModel.isDataRefreshing.observe(viewLifecycleOwner, ::stopRefresher)
        viewModel.responseErrorMessage.observe(viewLifecycleOwner, ::showResponseErrorMessage)
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun updateRepositories(repositories : ArrayList<RepositoryItem>?) {
        if (repositories != null) {
            adapter.repositoriesList = repositories
        } else {
            adapter.repositoriesList = ArrayList()
        }
    }

    private fun setFirstPageButton(pageNum : Int?) {
        with(binding.bFirstPage.bPage) {
            if (pageNum != null) {
                isClickable = true
                setOnClickListener {
                    viewModel.loadPage(pageNum) }
            } else {
                isClickable = false
            }
        }
    }

    private fun setLastPageButton(pageNum : Int?) {
        with(binding.bLastPage.bPage) {
            if (pageNum != null) {
                isClickable = true
                setOnClickListener {
                    viewModel.loadPage(pageNum) }
            } else {
                isClickable = false
            }
        }
    }

    private fun setCurrentPagePosition(currentPagePosition : Int?) {
        val pageViews = listOf(
                binding.bPageSelector1.bPage,
                binding.bPageSelector2.bPage,
                binding.bPageSelector3.bPage,
                binding.bPageSelector4.bPage,
                binding.bPageSelector5.bPage
        )
        if (currentPagePosition != null) {
            for (i in 0 until 5) {
                if (i == currentPagePosition) {
                    pageViews[i].isClickable = false
                    pageViews[i].setBackgroundResource(R.drawable.shape_selected_page_button)
                    context?.let { pageViews[i].setTextColor(ContextCompat.getColor(it, R.color.white))}
                } else {
                    pageViews[i].isClickable = true
                    pageViews[i].setBackgroundResource(R.drawable.shape_navigation_bar_button)
                    context?.let { pageViews[i].setTextColor(ContextCompat.getColor(it, R.color.black))}
                }
            }
        }
    }

    private fun setAvailablePages(availablePages : List<Int>?) {
        val pageViews = listOf(
                binding.bPageSelector1.bPage,
                binding.bPageSelector2.bPage,
                binding.bPageSelector3.bPage,
                binding.bPageSelector4.bPage,
                binding.bPageSelector5.bPage
        )
        if (availablePages != null) {
            if (availablePages.size == 1) {
                binding.clNavigationBar.visibility = View.GONE
            } else {
                binding.clNavigationBar.visibility = View.VISIBLE
                for (i in availablePages.indices) {
                    pageViews[i].isClickable = true
                    pageViews[i].visibility = View.VISIBLE
                    pageViews[i].setOnClickListener {
                        viewModel.loadPage(availablePages[i]) }
                    pageViews[i].text = availablePages[i].toString()
                }
                for (i in availablePages.size until 5) {
                    pageViews[i].isClickable = false
                    pageViews[i].visibility = View.GONE
                }
            }
        }
    }

    private fun stopRefresher(isDataRefreshing : Boolean?) {
        if (isDataRefreshing != null) binding.srlRepositories.isRefreshing = isDataRefreshing
    }

    private fun showResponseErrorMessage(responseErrorMessage : String?) {
        if (responseErrorMessage != null) Toast.makeText(context, responseErrorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val firstPageNum = viewModel.firstPageNum.value
        val lastPageNum = viewModel.lastPageNum.value
        val availablePages = viewModel.availablePages.value
        val repositories = viewModel.repositories.value
        val currentPagePosition = viewModel.currentPagePosition.value
        val isDataRefreshing = viewModel.isDataRefreshing.value
        val currentPage = viewModel.currentPage.value

        if (firstPageNum != null) outState.putInt("firstPageNum", firstPageNum)
        if (lastPageNum != null) outState.putInt("lastPageNum", lastPageNum)
        if (availablePages != null) outState.putIntegerArrayList("availablePages", availablePages)
        if (repositories != null) outState.putParcelableArrayList("repositories", repositories)
        if (currentPagePosition != null) outState.putInt("currentPagePosition", currentPagePosition)
        if (isDataRefreshing != null) outState.putBoolean("isDataRefreshing", isDataRefreshing)
        if (currentPage != null) outState.putInt("currentPage", currentPage)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) viewModel.restoreRepositoriesData(savedInstanceState)
    }
}