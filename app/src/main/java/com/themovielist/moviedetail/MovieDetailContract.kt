package com.themovielist.moviedetail

import com.themovielist.base.BasePresenter
import com.themovielist.model.MovieModel
import com.themovielist.model.MovieReviewModel
import com.themovielist.model.MovieTrailerModel

/**
 * Presenter of the Movie Detail Fragment.
 */

abstract class MovieDetailContract {
    interface View {
        fun showMovieReview(movieReviewModelList: List<MovieReviewModel>)

        fun showMovieDetail(movieModel: MovieModel)

        fun showMovieTrailer(movieTrailerList: List<MovieTrailerModel>)

        fun setFavoriteButtonState(favorite: Boolean)

        fun showSuccessMessageAddFavoriteMovie()

        fun showSuccessMessageRemoveFavoriteMovie()


        fun showErrorMessageAddFavoriteMovie()

        fun showErrorMessageRemoveFavoriteMovie()

        fun showErrorMessageLoadReviews()

        fun showErrorMessageLoadTrailers()

        fun setShowAllReviewsButtonVisibility(visible: Boolean)

        fun setShowAllTrailersButtonVisibility(visible: Boolean)

        fun showLoadingReviewsIndicator()

        fun showLoadingTrailersIndicator()

        fun showAllReviews(movieReviewList: List<MovieReviewModel>, hasMore: Boolean)

        fun showAllTrailers(movieTrailerList: List<MovieTrailerModel>)

        fun showEmptyReviewListMessage()

        fun showEmptyTrailerListMessage()
    }

    interface Presenter : BasePresenter<View> {
        fun start(movieModel: MovieModel)

        fun removeFavoriteMovie(movieModel: MovieModel)

        fun saveFavoriteMovie(movieModel: MovieModel)

        fun showAllReviews()

        fun showAllTrailers()

        fun tryToLoadTrailersAgain()

        fun tryToLoadReviewAgain()
    }
}