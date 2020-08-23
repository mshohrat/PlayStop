package com.ms.playstop.ui.movie

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.add
import com.ms.playstop.extension.addToParent
import com.ms.playstop.extension.separateLengthByTime
import com.ms.playstop.extension.show
import com.ms.playstop.ui.comment.CommentsFragment
import com.ms.playstop.ui.movie.adapter.CommentAdapter
import com.ms.playstop.ui.movie.adapter.LinkAdapter
import com.ms.playstop.utils.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : BaseFragment() {

    companion object {
        fun newInstance() = MovieFragment()
        const val MOVIE_ID_KEY = "MOVIE ID KEY"
    }

    private lateinit var viewModel: MovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun tag(): String {
        return "Movie Fragment"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        subscribeToViewModel()
        subscribeToViewEvents()
        val movieId = arguments?.takeIf { it.containsKey(MOVIE_ID_KEY) }?.getInt(MOVIE_ID_KEY) ?: -1
        viewModel.fetchMovie(movieId)
    }

    private fun subscribeToViewModel() {
        viewModel.movie.observe(viewLifecycleOwner, Observer {
            it?.let { movie ->
                movie_name_tv?.text = movie.name
                movie.genres?.let {
                    var genres = ""
                    for (genre in it) {
                        genres = "$genres${genre.name} - "
                    }
                    genres = genres.dropLast(3)
                    movie_genre_tv?.text = genres
                }
                movie_length_tv?.text = separateLengthByTime(movie.length)
                movie_score_tv?.text = String.format(getString(R.string.imdb_score_x),movie.score?.toString())
                movie_score_number_tv?.text = movie.scoreVotes?.toString()
                movie_image_iv?.let {
                    Glide.with(it).load(movie.image).apply(
                        RequestOptions.bitmapTransform(
                            RoundedCornersTransformation(16,0)
                        )).into(it)
                }
                movie_description_tv?.text = movie.description
                movie_director_tv?.text = movie.director?.name
                movie_writer_tv?.text = movie.writer?.name
                movie.actors?.let {
                    var actors = ""
                    for (actor in it) {
                        actors = "$actors${actor.name} - "
                    }
                    actors = actors.dropLast(3)
                    movie_actors_tv?.text = actors
                }
                movie.urls?.takeIf { it.isNotEmpty() }?.let {
                    movie_links_title_tv?.show()
                    movie_links_recycler?.show()
                    val linkLayoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
                    val linkAdapter = LinkAdapter(it)
                    movie_links_recycler?.addItemDecoration(DividerItemDecoration(activity,RecyclerView.VERTICAL))
                    movie_links_recycler?.layoutManager = linkLayoutManager
                    movie_links_recycler?.adapter = linkAdapter
                }
                //todo show seasons if it is series
                movie.comments?.takeIf { it.isNotEmpty() }?.let {
                    movie_comments_title_tv?.show()
                    movie_comments_recycler?.show()
                    val commentLayoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
                    val commentAdapter = CommentAdapter(it)
                    movie_comments_recycler?.addItemDecoration(DividerItemDecoration(activity,RecyclerView.VERTICAL))
                    movie_comments_recycler?.layoutManager = commentLayoutManager
                    movie_comments_recycler?.adapter = commentAdapter
                    movie_show_all_comments_tv?.show()
                }
            }
        })

        viewModel.movieError.observe(viewLifecycleOwner, Observer {
            it.messageResId?.let {
                Toast.makeText(activity,it,Toast.LENGTH_SHORT).show()
            } ?: kotlin.run {
                Toast.makeText(activity,it.message ?: "",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun subscribeToViewEvents() {
        movie_show_all_comments_tv?.setOnClickListener {
            addToParent(CommentsFragment.newInstance())
        }

        movie_back_btn?.setOnClickListener {
            activity?.onBackPressed()
        }
    }

}
