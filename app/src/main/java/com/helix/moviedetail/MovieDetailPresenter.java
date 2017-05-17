package com.helix.moviedetail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.helix.data.Credits;
import com.helix.data.Images;
import com.helix.data.Movie;
import com.helix.data.MovieDetail;
import com.helix.data.source.MoviesDataSource;
import com.helix.data.source.MoviesRepository;
import javax.inject.Inject;

class MovieDetailPresenter implements MovieDetailContract.Presenter {

  private final MoviesRepository repository;

  private final MovieDetailContract.View movieDetailView;

  private int movieId;

  private boolean firstLoad = true;

  @Inject MovieDetailPresenter(MoviesRepository tasksRepository,
      MovieDetailContract.View movieDetailView) {
    repository = tasksRepository;
    this.movieDetailView = movieDetailView;
  }

  @Inject void setupListeners() {
    movieDetailView.setPresenter(this);
  }

  @Override public void setMovieId(int id) {
    this.movieId = id;
  }

  @Override public void loadMovieDetail(int movieId, boolean forceUpdate) {
    loadMovieDetail(forceUpdate || firstLoad, true, movieId);
    firstLoad = false;
  }

  @Override public void start() {
    loadMovieBrief(movieId);
    loadImages(movieId, false);
    loadMovieDetail(movieId, false);
    loadCredits(movieId, false);
  }

  private void loadMovieBrief(int movieId) {

    repository.getMovieBrief(new MoviesDataSource.FetchMovieBriefCallback() {
      @Override public void onMovieLoaded(Movie movie) {
        if (!movieDetailView.isActive()) {
          return;
        }
        movieDetailView.showMovieBrief(movie);
      }

      @Override public void onDataNotAvailable() {
        if (!movieDetailView.isActive()) {
          return;
        }
        movieDetailView.showLoadingMovieDetailError();
      }
    }, movieId);
  }

  @Override public void loadImages(int movieId, boolean forceUpdate) {

    repository.getImages(new MoviesDataSource.FetchMovieImagesCallback() {
      @Override public void onImagesLoaded(Images images) {
        if (!movieDetailView.isActive()) {
          return;
        }
        processImages(images);
      }

      @Override public void onDataNotAvailable() {
        if (!movieDetailView.isActive()) {
          return;
        }
        movieDetailView.showLoadingImagesError();
      }
    }, movieId);
  }

  @Override public void loadCredits(int movieId, boolean forceUpdate) {

    repository.getCredits(new MoviesDataSource.FetchMovieCreditsCallback() {
      @Override public void onCreditsLoaded(Credits credits) {
        if (!movieDetailView.isActive()) {
          return;
        }

        processCredits(credits);
      }

      @Override public void onDataNotAvailable() {
        if (!movieDetailView.isActive()) {
          return;
        }
        movieDetailView.showLoadingCreditsError();
      }
    }, movieId);
  }

  @Override public void loadImdbPage(String imdbId, Context context) {
    String imdbUrl = "http://www.imdb.com/title/" + imdbId + "/";
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(imdbUrl));
    context.startActivity(browserIntent);
  }

  @Override public void stop() {

  }

  private void loadMovieDetail(boolean forceUpdate, boolean showLoadingUI, int movieId) {
    if (showLoadingUI) {
      movieDetailView.setLoadingIndicator(true);
    }
    if (forceUpdate) {
      repository.refreshMovies();
    }

    repository.getMovieDetail(new MoviesDataSource.FetchMovieDetailCallback() {

      @Override public void onMovieLoaded(MovieDetail movieDetail) {
        if (!movieDetailView.isActive()) {
          return;
        }
        if (showLoadingUI) {
          movieDetailView.setLoadingIndicator(false);
        }

        processMovie(movieDetail);
      }

      @Override public void onDataNotAvailable() {
        if (!movieDetailView.isActive()) {
          return;
        }
        movieDetailView.showLoadingMovieDetailError();
      }
    }, movieId);
  }

  private void processMovie(MovieDetail movieDetail) {
    movieDetailView.showMovieDetail(movieDetail);
  }

  private void processImages(Images images) {
    movieDetailView.showImages(images);
  }

  private void processCredits(Credits credits) {
    movieDetailView.showCredits(credits);
  }
}
