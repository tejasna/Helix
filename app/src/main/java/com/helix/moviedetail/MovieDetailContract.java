package com.helix.moviedetail;

import android.content.Context;
import com.helix.BasePresenter;
import com.helix.BaseView;
import com.helix.data.Credits;
import com.helix.data.Images;
import com.helix.data.Movie;
import com.helix.data.MovieDetail;

class MovieDetailContract {

  interface View extends BaseView<Presenter> {

    void showMovieBrief(Movie movie);

    void showMovieDetail(MovieDetail movieDetail);

    void showImages(Images images);

    void showCredits(Credits credits);

    void showLoadingImagesError();

    void showLoadingCreditsError();

    void setLoadingIndicator(boolean active);

    void showNoMovieDetail();

    void showLoadingMovieDetailError();

    boolean isActive();
  }

  interface Presenter extends BasePresenter {

    void setMovieId(int id);

    void loadMovieDetail(int movieId, boolean forceUpdate);

    void loadImages(int movieId, boolean forceUpdate);

    void loadCredits(int movieId, boolean forceUpdate);

    void loadImdbPage(String imdbId, Context context);
  }
}
