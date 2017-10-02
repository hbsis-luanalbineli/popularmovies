package com.albineli.udacity.popularmovies.movielist;

import android.support.annotation.NonNull;

import com.albineli.udacity.popularmovies.base.BasePresenterImpl;
import com.albineli.udacity.popularmovies.enums.MovieListFilterDescriptor;
import com.albineli.udacity.popularmovies.model.MovieModel;
import com.albineli.udacity.popularmovies.repository.ArrayRequestAPI;
import com.albineli.udacity.popularmovies.repository.movie.MovieRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Presenter of the movie list fragment.
 */

public class MovieListPresenter extends BasePresenterImpl implements MovieListContract.Presenter {
    private MovieListContract.View mView;
    private @MovieListFilterDescriptor.MovieListFilter
    int mFilter;

    private boolean mHasError = false;
    private Disposable mSubscription;
    /*
        The unitial page must be 1 (API implementation).
     */
    private int mPageIndex = 1;
    private int mSelectedMovieIndex;

    @Inject
    MovieListPresenter(@NonNull MovieRepository movieRepository) {
        super(movieRepository);
    }

    @Override
    public void setView(MovieListContract.View view) {
        mView = view;
    }

    @Override
    public void init(@MovieListFilterDescriptor.MovieListFilter int filter) {
        loadMovieList(true);
    }

    @Override
    public void onStop() {
        if (mSubscription != null && !mSubscription.isDisposed()) {
            mSubscription.dispose();
        }
    }

    private void loadMovieList(final boolean startOver, @MovieListFilterDescriptor.MovieListFilter int filter) {
        Timber.i("loadMovieList - Loading the movie list");
        if (mSubscription != null) {
            return;
        }

        mView.showLoadingIndicator();

        if (startOver) {
            mPageIndex = 1;
        } else {
            mPageIndex++;
        }

        Observable<ArrayRequestAPI<MovieModel>> observable;
        if (filter == MovieListFilterDescriptor.POPULAR) {
            observable = mMovieRepository.getPopularList(mPageIndex);
        } else if (filter == MovieListFilterDescriptor.RATING) {
            observable = mMovieRepository.getTopRatedList(mPageIndex);
        } else {
            observable = mMovieRepository.getFavoriteList();
        }

        mSubscription = observable
                .doOnTerminate(() -> mSubscription = null)
                .subscribe(
                    response -> handleSuccessLoadMovieList(response, startOver),
                    this::handleErrorLoadMovieList);
    }

    private void handleSuccessLoadMovieList(ArrayRequestAPI<MovieModel> response, boolean startOver) {
        Timber.i("handleSuccessLoadMovieList - CHANGED");
        if (response.results.size() == 0) {
            mView.clearMovieList(); // Make sure that the list is empty.
            mView.showEmptyListMessage();
        } else {
            mView.showMovieList(response.results, startOver);
        }

        if (response.hasMorePages()) {
            mView.enableLoadMoreListener();
        } else {
            mView.disableLoadMoreListener();
        }
    }

    private void handleErrorLoadMovieList(Throwable throwable) {
        mHasError = true;
        Timber.e(throwable, "An error occurred while tried to get the movies");

        if (mPageIndex > 1) { // If something got wrong, reverse to the original position.
            mPageIndex--;
        }

        if (mFilter == MovieListFilterDescriptor.FAVORITE) {
            mView.clearMovieList();
        }

        mView.showLoadingMovieListError();
    }

    @Override
    public void loadMovieList(final boolean startOver) {
        loadMovieList(startOver, mFilter);
    }

    @Override
    public void setFilter(@MovieListFilterDescriptor.MovieListFilter int movieListFilter) {
        if (mFilter == movieListFilter) { // If it's the same order, do nothing.
            return;
        }

        if (mSubscription != null && !mSubscription.isDisposed()) {
            mSubscription.dispose();
            mSubscription = null;
        }

        mFilter = movieListFilter;
        // Reload the movie list.
        loadMovieList(true);

        mView.setTitleByFilter(movieListFilter);
    }

    @Override
    public void openMovieDetail(int selectedMovieIndex, MovieModel movieModel) {
        mSelectedMovieIndex = selectedMovieIndex;
        mView.showMovieDetail(movieModel);
    }

    @Override
    public void onListEndReached() {
        if (mHasError) {
            return;
        }
        loadMovieList(false);
    }

    @Override
    public void tryAgain() {
        mHasError = false;
        mView.hideRequestStatus();

        loadMovieList(false);
    }

    @Override
    public void favoriteMovie(MovieModel movie, boolean favorite) {
        if (mFilter != MovieListFilterDescriptor.FAVORITE) { // Only if the user is at favorite list it needs an update.
            return;
        }

        if (favorite) {
            mView.addMovieToListByIndex(mSelectedMovieIndex, movie);
        } else {
            mView.removeMovieFromListByIndex(mSelectedMovieIndex);
        }

        if (mView.getMovieListCount() == 0) {
            mView.showEmptyListMessage();
        }
    }

    @Override
    public void resume() {
        mView.setTitleByFilter(mFilter);
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
        if (visible) {
            mView.setTitleByFilter(mFilter);
        }
    }
}
