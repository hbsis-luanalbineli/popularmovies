package com.albineli.udacity.popularmovies.moviedetail.trailer;

import android.view.View;
import android.widget.TextView;

import com.albineli.udacity.popularmovies.ui.recyclerview.CustomRecyclerViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

public class MovieTrailerViewHolder extends CustomRecyclerViewHolder {
    // @BindView(R.id.sdvMovieTrailerThumbnail)
    SimpleDraweeView mThumbnailSimpleDraweeView;

    // @BindView(R.id.tvMovieTrailerName)
    TextView mNameTextView;

    // @BindView(R.id.tvMovieTrailerSize)
    TextView mSizeTextView;

    // @BindView(R.id.tvMovieTrailerSource)
    TextView mSourceTextView;

    MovieTrailerViewHolder(View itemView) {
        super(itemView);

        // ButterKnife.bind(this, itemView);
    }
}
