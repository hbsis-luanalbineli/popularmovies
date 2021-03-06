package com.albineli.udacity.popularmovies.moviedetail.trailer

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.albineli.udacity.popularmovies.PopularMovieApplication
import com.albineli.udacity.popularmovies.R
import com.albineli.udacity.popularmovies.base.BaseFullscreenDialogWithList
import com.albineli.udacity.popularmovies.base.BasePresenter
import com.albineli.udacity.popularmovies.injector.components.ApplicationComponent
import com.albineli.udacity.popularmovies.injector.components.DaggerFragmentComponent
import com.albineli.udacity.popularmovies.model.MovieTrailerModel
import com.albineli.udacity.popularmovies.util.YouTubeUtil

import javax.inject.Inject

class MovieTrailerListDialog : BaseFullscreenDialogWithList<MovieTrailerModel, MovieTrailerListDialogContract.View>(), MovieTrailerListDialogContract.View {
    private val mMovieReviewAdapter by lazy { MovieTrailerAdapter() }

    private val mLinearLayoutManager by lazy { LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false) }

    @Inject
    lateinit var mPresenter: MovieTrailerListDialogPresenter

    override val presenterImplementation: BasePresenter<MovieTrailerListDialogContract.View>
        get() = mPresenter

    override val viewImplementation: MovieTrailerListDialogContract.View
        get() = this

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fullscreenDialogWithListView = super.onCreateView(inflater, container, savedInstanceState)

        mMovieReviewAdapter.setOnItemClickListener { _, item -> YouTubeUtil.openYouTubeVideo(activity, item.key) }

        val dividerItemDecoration = DividerItemDecoration(recyclerView.getContext(), mLinearLayoutManager.orientation)

        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.layoutManager = mLinearLayoutManager
        recyclerView.adapter = mMovieReviewAdapter

        return fullscreenDialogWithListView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPresenter!!.start(mList)

        setTitle(R.string.all_trailers)
    }

    override fun showTrailersIntoList(movieReviewList: List<MovieTrailerModel>) {
        mMovieReviewAdapter.addItems(movieReviewList)
    }

    override fun onInjectDependencies(applicationComponent: ApplicationComponent) {
        DaggerFragmentComponent.builder()
                .applicationComponent(PopularMovieApplication.getApplicationComponent(activity))
                .build()
                .inject(this)
    }

    companion object {

        fun getInstance(movieModelList: List<MovieTrailerModel>): MovieTrailerListDialog {
            return BaseFullscreenDialogWithList.createNewInstance(MovieTrailerListDialog::class.java, movieModelList)
        }
    }
}
