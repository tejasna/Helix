package com.helix.data.source;

import android.support.annotation.NonNull;
import com.helix.data.MovieDetail;
import com.helix.data.Upcoming;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import static com.helix.utils.PreConditions.checkNotNull;

@HelixApplicationScope public class MoviesRepository implements MoviesDataSource {

  private final MoviesDataSource remoteDataSource;

  private final MoviesDataSource localDataSource;

  private Upcoming cachedUpcomingMovies;

  private List<MovieDetail> cachedUpcomingMovieDetail;

  private boolean cacheIsDirty = false;

  @Inject MoviesRepository(@Remote MoviesDataSource tasksRemoteDataSource,
      @Local MoviesDataSource tasksLocalDataSource) {
    remoteDataSource = tasksRemoteDataSource;
    localDataSource = tasksLocalDataSource;
  }

  @SuppressWarnings("NullableProblems") @Override
  public void getUpcomingMovies(@NonNull FetchUpcomingMoviesCallback callback, @NonNull int page) {
    if (cachedUpcomingMovies != null && !cacheIsDirty) {
      callback.onMoviesLoaded(cachedUpcomingMovies, cachedUpcomingMovieDetail);
      return;
    }

    if (cacheIsDirty) {
      getMoviesFromRemoteDataSource(callback, page);
    } else {
      localDataSource.getUpcomingMovies(new FetchUpcomingMoviesCallback() {

        @Override
        public void onMoviesLoaded(Upcoming upcomingMovies, List<MovieDetail> upcomingMovieDetail) {
          checkNotNull(upcomingMovies);
          checkNotNull(upcomingMovieDetail);
          refreshCache(upcomingMovies);
          refreshCache(upcomingMovieDetail);
          callback.onMoviesLoaded(upcomingMovies, upcomingMovieDetail);
        }

        @Override public void onDataNotAvailable() {
          getMoviesFromRemoteDataSource(callback, page);
        }
      }, page);
    }
  }

  @Override public void saveUpcomingMovies(@NonNull Upcoming upcomingMovies) {
    localDataSource.saveUpcomingMovies(upcomingMovies);
  }

  @Override public void saveUpcomingMovieDetails(@NonNull List<MovieDetail> upcomingMovieDetail) {
    localDataSource.saveUpcomingMovieDetails(upcomingMovieDetail);
  }

  @Override public void refreshMovies() {
    cacheIsDirty = true;
  }

  private void getMoviesFromRemoteDataSource(@NonNull final FetchUpcomingMoviesCallback callback,
      int page) {
    remoteDataSource.getUpcomingMovies(new FetchUpcomingMoviesCallback() {

      @Override
      public void onMoviesLoaded(Upcoming upcomingMovies, List<MovieDetail> upcomingMovieDetail) {
        checkNotNull(upcomingMovies);
        checkNotNull(upcomingMovieDetail);
        refreshCache(upcomingMovies);
        refreshCache(upcomingMovieDetail);
        localDataSource.saveUpcomingMovies(upcomingMovies);
        localDataSource.saveUpcomingMovieDetails(upcomingMovieDetail);
        callback.onMoviesLoaded(upcomingMovies, upcomingMovieDetail);
      }

      @Override public void onDataNotAvailable() {
        callback.onDataNotAvailable();
        localDataSource.getUpcomingMovies(new FetchUpcomingMoviesCallback() {

          @Override public void onMoviesLoaded(Upcoming upcomingMovies,
              List<MovieDetail> upcomingMovieDetail) {
            checkNotNull(upcomingMovies);
            checkNotNull(upcomingMovieDetail);
            refreshCache(upcomingMovies);
            refreshCache(upcomingMovieDetail);
            callback.onMoviesLoaded(upcomingMovies, upcomingMovieDetail);
          }

          @Override public void onDataNotAvailable() {
            callback.onDataNotAvailable();
          }
        }, page);
      }
    }, page);
  }

  private void refreshCache(Upcoming upcomingMovies) {
    if (cachedUpcomingMovies == null) {
      cachedUpcomingMovies = new Upcoming();
    }
    this.cachedUpcomingMovies = upcomingMovies;
    cacheIsDirty = false;
  }

  private void refreshCache(List<MovieDetail> upcomingMovies) {
    if (cachedUpcomingMovieDetail == null) {
      cachedUpcomingMovieDetail = new ArrayList<>();
    }
    this.cachedUpcomingMovieDetail = upcomingMovies;
    cacheIsDirty = false;
  }

  @Override public void clearSubscriptions() {
    remoteDataSource.clearSubscriptions();
  }
}