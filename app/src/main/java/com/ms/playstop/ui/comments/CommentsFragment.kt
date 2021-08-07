package com.ms.playstop.ui.comments

import android.content.res.ColorStateList
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.addToParent
import com.ms.playstop.extension.hide
import com.ms.playstop.extension.isUserLoggedIn
import com.ms.playstop.extension.show
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.ui.comments.adapter.CommentPagedAdapter
import com.ms.playstop.ui.enrerPhoneNumber.EnterPhoneNumberFragment
import com.ms.playstop.ui.movies.MoviesFragment
import com.ms.playstop.utils.DayNightModeAwareAdapter
import kotlinx.android.synthetic.main.fragment_comments.*
import kotlinx.android.synthetic.main.fragment_movies.*

class CommentsFragment : BaseFragment() {

    companion object {
        fun newInstance() = CommentsFragment()
        const val COMMENTS_MOVIE_ID = "COMMENTS MOVIE ID"
    }

    private lateinit var viewModel: CommentsViewModel

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
    }

    override fun onDayNightModeApplied(type: Int) {
        activity?.let { ctx ->
            with(ContextCompat.getColor(ctx,R.color.colorPrimary)){
                view?.setBackgroundColor(this)
                comments_submit_comment_btn?.setBackgroundColor(this)
            }
            comments_appbar?.setBackgroundColor(ContextCompat.getColor(ctx,R.color.colorAccentDark))
            comments_submit_comment_btn?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.colorAccent))
            comments_submit_comment_et?.setTextColor(ContextCompat.getColor(ctx,R.color.white))
            comments_submit_comment_et?.setHintTextColor(ContextCompat.getColor(ctx,R.color.gray))
            comments_loading?.setBackgroundColor(ContextCompat.getColor(ctx,R.color.white_opacity_10))
            comments_recycler?.adapter?.takeIf { it is DayNightModeAwareAdapter }?.let {
                (it as DayNightModeAwareAdapter).onDayNightModeChanged(type)
            }
            comments_progress?.setColor(ContextCompat.getColor(ctx,R.color.grayLight))
        }
    }

    override fun onViewLoaded() {
        super.onViewLoaded()
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
                showLoading()
                viewModel.sendComment(comments_submit_comment_et?.text?.toString())
            } else {
                val enterPhoneNumberFragment = EnterPhoneNumberFragment.newInstance()
                val args = Bundle().apply {
                    putInt(EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE, EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE_LOGIN)
                }
                enterPhoneNumberFragment.arguments = args
                addToParent(enterPhoneNumberFragment)
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
            dismissLoading()
        })

        viewModel.sendCommentError.observe(viewLifecycleOwner, Observer {
            showToast(it)
            dismissLoading()
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

    private fun showLoading() {
        comments_loading?.show()
    }

    private fun dismissLoading() {
        comments_loading?.hide()
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