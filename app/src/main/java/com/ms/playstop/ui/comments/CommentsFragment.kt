package com.ms.playstop.ui.comments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.addToParent
import com.ms.playstop.extension.isUserLoggedIn
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.ui.comments.adapter.CommentPagedAdapter
import com.ms.playstop.ui.login.LoginFragment
import com.ms.playstop.ui.movies.MoviesFragment
import com.ms.playstop.utils.LoadingDialog
import kotlinx.android.synthetic.main.fragment_comments.*
import kotlinx.android.synthetic.main.fragment_movies.*

class CommentsFragment : BaseFragment() {

    companion object {
        fun newInstance() = CommentsFragment()
        const val COMMENTS_MOVIE_ID = "COMMENTS MOVIE ID"
    }

    private lateinit var viewModel: CommentsViewModel
    private var loadingDialog: LoadingDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comments, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        viewModel = ViewModelProviders.of(this).get(CommentsViewModel::class.java)
        val movieId = arguments?.takeIf { it.containsKey(COMMENTS_MOVIE_ID) }
            ?.getInt(COMMENTS_MOVIE_ID) ?: -1
        viewModel.setMovieId(movieId)
        subscribeToViewEvents()
        subscribeToViewModel()
    }

    private fun subscribeToViewEvents() {
        comments_back_btn?.setOnClickListener {
            activity?.onBackPressed()
        }
        comments_submit_comment_btn?.setOnClickListener {
            if(isUserLoggedIn()) {
                showLoadingDialog()
                viewModel.sendComment(comments_submit_comment_et?.text?.toString())
            } else {
                val loginFragment = LoginFragment.newInstance()
                addToParent(loginFragment)
            }
        }
    }

    private fun subscribeToViewModel() {
        viewModel.comments.observe(viewLifecycleOwner, Observer { list ->
            comments_recycler?.adapter?.takeIf { it is CommentPagedAdapter }?.let {
                (it as CommentPagedAdapter).submitList(list)
            }
        })

        viewModel.commentsError.observe(viewLifecycleOwner, Observer {
            showToast(it)
        })

        viewModel.sendComment.observe(viewLifecycleOwner, Observer {
            showToast(it)
            comments_submit_comment_et?.text = null
            dismissLoadingDialog()
        })

        viewModel.sendCommentError.observe(viewLifecycleOwner, Observer {
            showToast(it)
        })
    }

    override fun tag(): String {
        return "Comments Fragment"
    }

    private fun initViews() {
        arguments?.takeIf { it.containsKey(MoviesFragment.MOVIES_REQUEST_NAME) }?.let {
            movies_toolbar_name_tv?.text = it.getString(MoviesFragment.MOVIES_REQUEST_NAME)
        }

        val layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
        comments_recycler?.layoutManager = layoutManager
        val adapter = CommentPagedAdapter()
        comments_recycler?.adapter = adapter
    }

    private fun showLoadingDialog() {
        activity?.let { ctx ->
            loadingDialog = LoadingDialog(ctx)
            loadingDialog?.show()
        }
    }

    private fun dismissLoadingDialog() {
        loadingDialog?.takeIf { it.isShowing }?.dismiss()
        loadingDialog?.cancel()
    }

    private fun showToast(response: GeneralResponse) {
        response.message?.takeIf { it.isNotEmpty() }?.let {
            Toast.makeText(activity,it,Toast.LENGTH_SHORT).show()
        } ?: kotlin.run {
            response.messageResId?.let {
                Toast.makeText(activity,it,Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

}