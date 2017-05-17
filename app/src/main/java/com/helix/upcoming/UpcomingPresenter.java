package com.helix.upcoming;

import android.util.SparseArray;
import com.helix.data.Genre;
import com.helix.data.Movie;
import com.helix.data.RealmInt;
import com.helix.data.Upcoming;
import com.helix.data.source.MoviesDataSource;
import com.helix.data.source.MoviesRepository;
import io.realm.Realm;
import io.realm.RealmList;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

class UpcomingPresenter implements UpcomingContract.Presenter {

  private final MoviesRepository repository;

  private final UpcomingContract.View upcomingView;

  private boolean firstLoad = true;

  private SparseArray<Genre> cachedGenres;

  @Inject UpcomingPresenter(MoviesRepository tasksRepository, UpcomingContract.View upcomingView) {
    repository = tasksRepository;
    this.upcomingView = upcomingView;
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
      @Override public void onMoviesLoaded(Upcoming upcomingMovies, List<Genre> genres) {
        if (!upcomingView.isActive()) {
          return;
        }
        if (showLoadingUI) {
          upcomingView.setLoadingIndicator(false);
        }

        processMovies(upcomingMovies, genres);
      }

      @Override public void onDataNotAvailable() {
        if (!upcomingView.isActive()) {
          return;
        }
        upcomingView.showLoadingMoviesError();
      }
    }, page);
  }

  private void processMovies(Upcoming upcomingMovies, List<Genre> genres) {

    if (upcomingMovies.getMovies() == null || upcomingMovies.getMovies().isEmpty()) {
      upcomingView.showNoMovies();
    } else {

      ArrayList<Movie> movies = new ArrayList<>(upcomingMovies.getMovies());

      if (cachedGenres == null) {
        cachedGenres = new SparseArray<>();
        for (Genre val : genres) {
          int key = val.getId();
          cachedGenres.put(key, val);
        }
      }

      for (Movie movie : movies) {

        RealmList<Genre> movieGenres = new RealmList<>();
        for (RealmInt genreId : movie.getGenreIds()) {

          movieGenres.add(cachedGenres.get(genreId.getValue()));
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        movie.setGenres(movieGenres);
        realm.commitTransaction();
        realm.close();
      }
      upcomingView.showMovies(new ArrayList<>(upcomingMovies.getMovies()));
    }
  }
}

