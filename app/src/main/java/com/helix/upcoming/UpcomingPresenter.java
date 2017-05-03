package com.helix.upcoming;

import com.helix.data.Movie;
import com.helix.data.MovieDetail;
import com.helix.data.Upcoming;
import com.helix.data.source.MoviesDataSource;
import com.helix.data.source.MoviesRepository;
import io.realm.Realm;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

class UpcomingPresenter implements UpcomingContract.Presenter {

  private final MoviesRepository repository;

  private final UpcomingContract.View upcomingView;

  private boolean firstLoad = true;

  @Inject UpcomingPresenter(MoviesRepository tasksRepository, UpcomingContract.View loginView) {
    repository = tasksRepository;
    this.upcomingView = loginView;
  }

  @Inject void setupListeners() {
    upcomingView.setPresenter(this);
  }

  @Override public void start() {
    loadMovies(false);
  }

  @Override public void stop() {
    repository.clearSubscriptions();
  }

  @Override public void loadMovies(boolean forceUpdate) {

    loadMovies(forceUpdate || firstLoad, true, 1, false);
    firstLoad = false;
  }

  @Override public void loadMore(int page) {
    loadMovies(true, false, page, true);
  }

  private void loadMovies(boolean forceUpdate, boolean showLoadingUI, int page,
      boolean isPaginate) {
    if (showLoadingUI) {
      upcomingView.setLoadingIndicator(true);
    }
    if (forceUpdate || isPaginate) {
      repository.refreshMovies();
    }

    repository.getUpcomingMovies(new MoviesDataSource.FetchUpcomingMoviesCallback() {

      @Override
      public void onMoviesLoaded(Upcoming upcomingMovies, List<MovieDetail> upcomingMovieDetail) {
        if (!upcomingView.isActive()) {
          return;
        }
        if (showLoadingUI) {
          upcomingView.setLoadingIndicator(false);
        }

        processMovies(upcomingMovies, upcomingMovieDetail);
      }

      @Override public void onDataNotAvailable() {
        if (!upcomingView.isActive()) {
          return;
        }
        upcomingView.showLoadingMoviesError();
      }
    }, page);
  }

  private void processMovies(Upcoming upcomingMovies, List<MovieDetail> movieDetails) {

    if (upcomingMovies.getMovies() != null && upcomingMovies.getMovies().isEmpty()) {
      upcomingView.showNoMovies();
    } else {
      ArrayList<Movie> movies = new ArrayList<>(upcomingMovies.getMovies());
      Realm realm = Realm.getDefaultInstance();
      for (Movie movie : movies) {
        for (MovieDetail movieDetail : movieDetails) {
          if (movie.getId() == movieDetail.getId()) {
            realm.beginTransaction();
            movie.setGenres(movieDetail.getGenres());
            realm.commitTransaction();
          }
        }
      }
      realm.close();
      upcomingView.showMovies(new ArrayList<>(upcomingMovies.getMovies()));
    }
  }
}

