package com.skorik05.autodoctest.domain

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skorik05.autodoctest.model.dagger.DaggerMainComponent
import com.skorik05.autodoctest.model.items.RepositoryItem
import com.skorik05.autodoctest.model.utils.parseHeader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

class SharedViewModel() : ViewModel() {
    private val daggerMainComponent = DaggerMainComponent.builder().build()
    private val repo = daggerMainComponent.getRepo()

    private val mutableRepositories = MutableLiveData<ArrayList<RepositoryItem>?>()
    private val mutableFirstPageNum = MutableLiveData<Int?>()
    private val mutableLastPageNum = MutableLiveData<Int?>()
    private val mutableAvailablePages = MutableLiveData<ArrayList<Int>?>()
    private val mutableCurrentPagePosition = MutableLiveData<Int?>()
    private val mutableIsDataRefreshing = MutableLiveData<Boolean?>()
    private val mutableCurrentPage = MutableLiveData<Int?>()
    private val mutableResponseErrorMessage = MutableLiveData<String?>()

    private val mutableLogin = MutableLiveData<String?>()
    private val mutableBio = MutableLiveData<String?>()
    private val mutableBlog = MutableLiveData<String?>()
    private val mutableTwitterUserName = MutableLiveData<String?>()
    private val mutableEmail = MutableLiveData<String?>()
    private val mutableFollowers = MutableLiveData<Int?>()
    private val mutableFollowing = MutableLiveData<Int?>()
    private val mutableAvatarUrl = MutableLiveData<String?>()
    private val mutableUserLoadingErrorOccurred = MutableLiveData<Boolean?>()

    val repositories: LiveData<ArrayList<RepositoryItem>?> = mutableRepositories
    val firstPageNum: LiveData<Int?> = mutableFirstPageNum
    val lastPageNum: LiveData<Int?> = mutableLastPageNum
    val availablePages: LiveData<ArrayList<Int>?> = mutableAvailablePages
    val currentPagePosition: LiveData<Int?> = mutableCurrentPagePosition
    val isDataRefreshing : LiveData<Boolean?> = mutableIsDataRefreshing
    val currentPage : LiveData<Int?> = mutableCurrentPage
    val responseErrorMessage: LiveData<String?> = mutableResponseErrorMessage

    val login : LiveData<String?> = mutableLogin
    val bio : LiveData<String?> = mutableBio
    val blog : LiveData<String?> = mutableBlog
    val twitterUserName : LiveData<String?> = mutableTwitterUserName
    val email : LiveData<String?> = mutableEmail
    val followers : LiveData<Int?> = mutableFollowers
    val following : LiveData<Int?> = mutableFollowing
    val avatarUrl : LiveData<String?> = mutableAvatarUrl
    val userLoadingErrorOccurred : LiveData<Boolean?> = mutableUserLoadingErrorOccurred
    private var currentSearchText : String? = null

    init {
        mutableAvailablePages.postValue(arrayListOf(1))
        mutableCurrentPagePosition.postValue(0)
        mutableFirstPageNum.postValue(null)
        mutableLastPageNum.postValue(null)
        mutableIsDataRefreshing.postValue(false)
        mutableCurrentPage.postValue(1)

        mutableLogin.postValue("")
        mutableBio.postValue("")
        mutableTwitterUserName.postValue("")
        mutableBlog.postValue("")
        mutableEmail.postValue("")
        mutableAvatarUrl.postValue(null)
        mutableUserLoadingErrorOccurred.postValue(false)
    }

    fun loadRepositories(searchText : String, pageNum : Int? = currentPage.value, perPage: Int = 10) {
        if (pageNum == null) return
        currentSearchText = searchText
        CoroutineScope(Dispatchers.Main).launch {
            val task = async(Dispatchers.IO) { repo.getRepositoriesByName(searchText, perPage, pageNum) }
            val response = task.await()
            when(response.code()) {
                200 -> {
                    val header = response.headers().get("Link")
                    val body = response.body()
                    if (header != null) {
                        val paginationLinks = parseHeader(header)
                        mutableFirstPageNum.postValue(paginationLinks.firstPageNum)
                        mutableLastPageNum.postValue(paginationLinks.lastPageNum)
                        with(paginationLinks.currentPage) {
                            if (this != null) {
                                var pages = ArrayList<Int>()
                                val currentPosition : Int?
                                when (this) {
                                    in 1..3 -> {
                                        for (i in 1..min(5, paginationLinks.totalPagesNum)) {
                                            pages.add(i)
                                        }
                                        currentPosition = this - 1
                                    }
                                    in paginationLinks.totalPagesNum-2..paginationLinks.totalPagesNum -> {
                                        for (i in max(1, paginationLinks.totalPagesNum - 4)..paginationLinks.totalPagesNum) {
                                            pages.add(i)
                                        }
                                        currentPosition = 4 - (paginationLinks.totalPagesNum - this)
                                    }
                                    else -> {
                                        pages = arrayListOf(this - 2, this - 1, this, this + 1, this + 2)
                                        currentPosition = 2
                                    }
                                }
                                mutableCurrentPage.postValue(this)
                                mutableAvailablePages.postValue(pages)
                                mutableCurrentPagePosition.postValue(currentPosition)
                            }
                        }
                    } else {
                        mutableCurrentPage.postValue(1)
                        mutableAvailablePages.postValue(arrayListOf(1))
                        mutableCurrentPagePosition.postValue(0)
                        mutableFirstPageNum.postValue(null)
                        mutableLastPageNum.postValue(null)
                    }
                    if (body != null) {
                        if (body.items != null) mutableRepositories.postValue(body.items)
                    }
                }
                403 -> mutableResponseErrorMessage.postValue("Превышено число запросов. Повторите позже.")
            }
            mutableIsDataRefreshing.postValue(false)
        }
    }

    fun loadPage(pageNum: Int) {
        currentSearchText?.let { loadRepositories(it, pageNum) }
    }

    fun restoreRepositoriesData(savedInstanceState : Bundle) {
        val firstPageNum = savedInstanceState.getInt("firstPageNum", -1)
        val lastPageNum = savedInstanceState.getInt("lastPageNum", -1)
        val availablePages = savedInstanceState.getIntegerArrayList("availablePages")
        val repositories = savedInstanceState.getParcelableArrayList<RepositoryItem>("repositories")
        val currentPagePosition = savedInstanceState.getInt("currentPagePosition", 0)
        val isDataRefreshing = savedInstanceState.getBoolean("isDataRefreshing", false)
        val currentPage = savedInstanceState.getInt("currentPage", 1)

        if (firstPageNum == -1) mutableFirstPageNum.postValue(null) else mutableFirstPageNum.postValue(firstPageNum)
        if (lastPageNum == -1) mutableLastPageNum.postValue(null) else mutableLastPageNum.postValue(lastPageNum)
        mutableAvailablePages.postValue(availablePages)
        mutableRepositories.postValue(repositories)
        mutableCurrentPagePosition.postValue(currentPagePosition)
        mutableIsDataRefreshing.postValue(isDataRefreshing)
        mutableCurrentPage.postValue(currentPage)
    }


    fun loadUserData(userUrl : String) {
        CoroutineScope(Dispatchers.Main).launch {
            val task = async(Dispatchers.IO) { repo.getUser(userUrl) }
            val response = task.await()
            when(response.code()) {
                200 -> {
                    val body = response.body()
                    if (body != null) {
                        mutableLogin.postValue(body.login)
                        mutableBio.postValue(body.bio)
                        mutableBlog.postValue(body.blog)
                        mutableTwitterUserName.postValue(body.twitterUsername)
                        mutableEmail.postValue(body.email)
                        mutableFollowers.postValue(body.followers)
                        mutableFollowing.postValue(body.following)
                        mutableAvatarUrl.postValue(body.avatarUrl)
                    }
                }
                else -> mutableUserLoadingErrorOccurred.postValue(true)
            }
        }
    }

    fun restoreUserData(savedInstanceState : Bundle) {
        mutableLogin.postValue(savedInstanceState.getString("login", ""))
        mutableBio.postValue(savedInstanceState.getString("bio", ""))
        mutableBlog.postValue(savedInstanceState.getString("blog", ""))
        mutableTwitterUserName.postValue(savedInstanceState.getString("twitterUserName", ""))
        mutableEmail.postValue(savedInstanceState.getString("email", ""))
        mutableFollowers.postValue(savedInstanceState.getInt("followers", 0))
        mutableFollowing.postValue(savedInstanceState.getInt("following", 0))
        if (savedInstanceState.getString("avatarUrl", "") == "") {
            mutableAvatarUrl.postValue(savedInstanceState.getString("avatarUrl"))
        }
    }
}