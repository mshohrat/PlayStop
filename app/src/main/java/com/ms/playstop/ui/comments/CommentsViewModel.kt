package com.ms.playstop.ui.comments

import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ms.playstop.R
import com.ms.playstop.model.Comment
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.network.model.PostCommentRequest
import com.ms.playstop.ui.comments.adapter.CommentDataFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class CommentsViewModel : ViewModel() {

    var comments : LiveData<PagedList<Comment>> = MutableLiveData()
    var commentsError : LiveData<GeneralResponse> = MutableLiveData()
    val sendComment : MutableLiveData<GeneralResponse> = MutableLiveData()
    val sendCommentError : MutableLiveData<GeneralResponse> = MutableLiveData()
    private var movieId: Int = 0

    fun setMovieId(movieId: Int) {
        this.movieId = movieId
        val commentDataFactory = CommentDataFactory(movieId)
        commentsError = Transformations.switchMap(commentDataFactory.getMutableLiveData(),
            Function {
                return@Function it.networkError
            }
        )
        val executor = Executors.newFixedThreadPool(5);
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(15)
            .setPageSize(10)
            .build()
        comments = LivePagedListBuilder(commentDataFactory,pagedListConfig).setFetchExecutor(executor).build()
    }

    fun sendComment(text: String?) {
        when {
            text.isNullOrEmpty() -> sendCommentError.value = GeneralResponse(messageResId = R.string.please_add_text_for_comment)
            else -> {
                ApiServiceGenerator.getApiService
                    .postComment(PostCommentRequest(text),movieId)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({
                        it?.let {
                            sendComment.value = GeneralResponse(messageResId = R.string.comment_posted_successfully)
                        } ?: kotlin.run {
                            sendCommentError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                        }
                    },{
                        sendCommentError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                    })
            }
        }
    }
}