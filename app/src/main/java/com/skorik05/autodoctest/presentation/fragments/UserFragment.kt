package com.skorik05.autodoctest.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.skorik05.autodoctest.R
import com.skorik05.autodoctest.databinding.FragmentUserBinding
import com.skorik05.autodoctest.domain.SharedViewModel
import com.skorik05.autodoctest.model.dagger.DaggerMainComponent

class UserFragment: Fragment() {

    private lateinit var binding: FragmentUserBinding
    private val viewModel: SharedViewModel by activityViewModels()
    val args: UserFragmentArgs by navArgs()
    private val daggerMainComponent = DaggerMainComponent.builder().build()
    private val picasso = daggerMainComponent.getPicasso()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.loadUserData(args.userUrl)
        }
        binding.toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        viewModel.login.observe(viewLifecycleOwner, ::setLogin)
        viewModel.bio.observe(viewLifecycleOwner, ::setBio)
        viewModel.blog.observe(viewLifecycleOwner, ::setBlog)
        viewModel.twitterUserName.observe(viewLifecycleOwner, ::setTwitterUserName)
        viewModel.email.observe(viewLifecycleOwner, ::setEmail)
        viewModel.following.observe(viewLifecycleOwner, ::setFollowing)
        viewModel.followers.observe(viewLifecycleOwner, ::setFollowers)
        viewModel.avatarUrl.observe(viewLifecycleOwner, ::setAvatar)
        viewModel.userLoadingErrorOccurred.observe(viewLifecycleOwner, ::closeUserPage)
    }

    private fun setLogin(login : String?) {
        if (login != null) {
            binding.tvLogin.text = login
        } else {
            binding.tvLogin.text = ""
        }
    }

    private fun setBio(bio : String?) {
        if (bio != null) {
            binding.tvBio.text = bio
        } else {
            binding.tvBio.text = ""

        }
    }

    private fun setBlog(blog : String?) {
        if (blog != null && blog.isNotEmpty()) {
            binding.tvBlog.visibility = View.VISIBLE
            binding.tvBlog.text = getString(R.string.blog, blog)
        } else {
            binding.tvBlog.visibility = View.GONE
        }
    }

    private fun setTwitterUserName(twitterUserName : String?) {
        if (twitterUserName != null && twitterUserName.isNotEmpty()) {
            binding.tvTwitterUsername.visibility = View.VISIBLE
            binding.tvTwitterUsername.text = getString(R.string.twitter_username, twitterUserName)
        } else {
            binding.tvTwitterUsername.visibility = View.GONE
        }
    }

    private fun setEmail(email : String?) {
        if (email != null && email.isNotEmpty()) {
            binding.tvEmail.visibility = View.VISIBLE
            binding.tvEmail.text = getString(R.string.email, email)
        } else {
            binding.tvEmail.visibility = View.GONE
        }
    }

    private fun setFollowing(following : Int?) {
        if (following != null) {
            binding.tvlFollowing.text = getString(R.string.following, following.toString())
        } else {
            binding.tvlFollowing.text = getString(R.string.following, "0")
        }

    }

    private fun setFollowers(followers : Int?) {
        if (followers != null) {
            binding.tvFollowers.text = getString(R.string.followers, followers.toString())
        } else {
            binding.tvlFollowing.text = getString(R.string.following, "0")
        }
    }

    private fun setAvatar(avatarUrl : String?) {
        if (avatarUrl != null) {
            binding.ivAvatar.visibility = View.VISIBLE
            picasso.load(avatarUrl).into(binding.ivAvatar)
        } else {
            binding.ivAvatar.visibility = View.INVISIBLE
        }
    }

    private fun closeUserPage(userLoadingErrorOccurred: Boolean?) {
        if (userLoadingErrorOccurred == true) activity?.onBackPressed()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val login = viewModel.login.value
        val bio = viewModel.bio.value
        val blog = viewModel.blog.value
        val twitterUserName = viewModel.twitterUserName.value
        val email = viewModel.email.value
        val following = viewModel.following.value
        val followers = viewModel.followers.value
        val avatarUrl = viewModel.avatarUrl.value

        if (login != null) outState.putString("login", login)
        if (bio != null) outState.putString("bio", bio)
        if (blog != null) outState.putString("blog", blog)
        if (twitterUserName != null) outState.putString("twitterUserName", twitterUserName)
        if (email != null) outState.putString("email", email)
        if (following != null) outState.putInt("following", following)
        if (followers != null) outState.putInt("followers", followers)
        if (avatarUrl != null) outState.putString("avatarUrl", avatarUrl)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) viewModel.restoreUserData(savedInstanceState)
    }
}