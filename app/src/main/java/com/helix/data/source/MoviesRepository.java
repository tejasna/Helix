package com.helix.data.source;

import android.support.annotation.NonNull;
import android.util.SparseArray;
import com.helix.data.Credits;
import com.helix.data.Genre;
import com.helix.data.Images;
import com.helix.data.Movie;
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

  private List<Genre> cachedGenres;

  private SparseArray<MovieDetail> cachedMovieDetails;

  private SparseArray<Movie> cachedMovies;

  private SparseArray<Images> cachedImages;

  private SparseArray<Credits> cachedCredits;

  private boolean cacheIsDirty = false;

  @Inject MoviesRepository(@Remote MoviesDataSource moviesRemoteDataSource,
      @Local MoviesDataSource moviesLocalDataSource) {
    remoteDataSource = moviesRemoteDataSource;
    localDataSource = moviesLocalDataSource;
  }

  @Override public void getUpcomingMovies(@NonNull FetchUpcomingMoviesCallback callback, int page) {
    if (cachedUpcomingMovies != null && !cacheIsDirty) {
      callback.onMoviesLoaded(cachedUpcomingMovies, cachedGenres);
      return;
    }

    if (cacheIsDirty) {
      getMoviesFromRemoteDataSource(callback, page);
    } else {
      localDataSource.getUpcomingMovies(new FetchUpcomingMoviesCallback() {

        @Override public void onMoviesLoaded(Upcoming upcomingMovies, List<Genre> genres) {
          checkNotNull(upcomingMovies);
          checkNotNull(genres);
          refreshCache(upcomingMovies);
          refreshCache(genres);
          callback.onMoviesLoaded(cachedUpcomingMovies, cachedGenres);
        }

        @Override public void onDataNotAvailable() {
          getMoviesFromRemoteDataSource(callback, page);
        }
      }, page);
    }
  }

  @Override public void getMovieDetail(@NonNull FetchMovieDetailCallback callback, int movieId) {
    if (cachedMovieDetails != null && !cacheIsDirty && cachedMovieDetails.get(movieId) != null) {
      callback.onMovieLoaded(cachedMovieDetails.get(movieId));
      return;
    }

    if (cacheIsDirty) {
      getMoviesFromRemoteDataSource(callback, movieId);
    } else {
      localDataSource.getMovieDetail(new FetchMovieDetailCallback() {

        @Override public void onMovieLoaded(MovieDetail movieDetail) {
          checkNotNull(movieDetail);
          refreshCache(movieDetail);
          callback.onMovieLoaded(movieDetail);
        }

        @Override public void onDataNotAvailable() {
          getMoviesFromRemoteDataSource(callback, movieId);
        }
      }, movieId);
    }
  }

  @Override public void getMovieBrief(@NonNull FetchMovieBriefCallback callback, int movieId) {

    if (cachedMovies != null && !cacheIsDirty && cachedMovies.get(movieId) != null) {
      callback.onMovieLoaded(cachedMovies.get(movieId));
    } else {
      localDataSource.getMovieBrief(new FetchMovieBriefCallback() {
        @Override public void onMovieLoaded(Movie movie) {
          checkNotNull(movie);
          refreshCache(movie);
          callback.onMovieLoaded(movie);
        }

        @Override public void onDataNotAvailable() {
          getMovieBrief(callback, movieId);
        }
      }, movieId);
    }
  }

  @Override public void getImages(@NonNull FetchMovieImagesCallback callback, int movieId) {
    if (cachedImages != null && !cacheIsDirty && cachedImages.get(movieId) != null) {
      callback.onImagesLoaded(cachedImages.get(movieId));
      return;
    }

    getImagesFromRemoteDataSource(callback, movieId);
  }

  @Override public void getCredits(@NonNull FetchMovieCreditsCallback callback, int movieId) {
    if (cachedCredits != null && !cacheIsDirty && cachedCredits.get(movieId) != null) {
      callback.onCreditsLoaded(cachedCredits.get(movieId));
      return;
    }

    if (cacheIsDirty) {
      getCreditsFromRemoteDataSource(callback, movieId);
    } else {
      localDataSource.getCredits(new FetchMovieCreditsCallback() {

        @Override public void onCreditsLoaded(Credits credits) {
          checkNotNull(credits);
          refreshCache(credits);
          callback.onCreditsLoaded(credits);
        }

        @Override public void onDataNotAvailable() {
          getCreditsFromRemoteDataSource(callback, movieId);
        }
      }, movieId);
    }
  }

  @Override public void saveUpcomingMovies(@NonNull Upcoming upcomingMovies) {
    localDataSource.saveUpcomingMovies(upcomingMovies);
  }

  @Override public void saveUpcomingMovieDetails(@NonNull MovieDetail movie) {
    localDataSource.saveUpcomingMovieDetails(movie);
  }

  @Override public void saveGenres(@NonNull List<Genre> genres) {
    localDataSource.saveGenres(genres);
  }

  @Override public void saveCredits(@NonNull Credits credits) {
    localDataSource.saveCredits(credits);
  }

  @Override public void refreshMovies() {
    cacheIsDirty = true;
  }

  private void getMoviesFromRemoteDataSource(@NonNull final FetchUpcomingMoviesCallback callback,
      int page) {
    remoteDataSource.getUpcomingMovies(new FetchUpcomingMoviesCallback() {

      @Override public void onMoviesLoaded(Upcoming upcomingMovies, List<Genre> genres) {
        checkNotNull(upcomingMovies);
        checkNotNull(genres);
        refreshCache(upcomingMovies);
        refreshCache(genres);
        localDataSource.saveUpcomingMovies(upcomingMovies);
        localDataSource.saveGenres(genres);
        callback.onMoviesLoaded(upcomingMovies, genres);
      }

      @Override public void onDataNotAvailable() {
        callback.onDataNotAvailable();
        localDataSource.getUpcomingMovies(new FetchUpcomingMoviesCallback() {

          @Override public void onMoviesLoaded(Upcoming upcomingMovies, List<Genre> genres) {
            checkNotNull(upcomingMovies);
            checkNotNull(genres);
            refreshCache(upcomingMovies);
            refreshCache(genres);
            callback.onMoviesLoaded(cachedUpcomingMovies, cachedGenres);
          }

          @Override public void onDataNotAvailable() {
            callback.onDataNotAvailable();
          }
        }, page);
      }
    }, page);
  }

  private void getImagesFromRemoteDataSource(FetchMovieImagesCallback callback, int movieId) {
    remoteDataSource.getImages(new FetchMovieImagesCallback() {
      @Override public void onImagesLoaded(Images images) {
        checkNotNull(images);
        refreshCache(images);
        callback.onImagesLoaded(cachedImages.get(movieId));
      }

      @Override public void onDataNotAvailable() {
        callback.onDataNotAvailable();
      }
    }, movieId);
  }

  private void getMoviesFromRemoteDataSource(@NonNull final FetchMovieDetailCallback callback,
      int movieId) {
    remoteDataSource.getMovieDetail(new FetchMovieDetailCallback() {

      @Override public void onMovieLoaded(MovieDetail movieDetail) {
        checkNotNull(movieDetail);
        refreshCache(movieDetail);
        localDataSource.saveUpcomingMovieDetails(movieDetail);
        callback.onMovieLoaded(cachedMovieDetails.get(movieId));
      }

      @Override public void onDataNotAvailable() {
        callback.onDataNotAvailable();
        localDataSource.getMovieDetail(new FetchMovieDetailCallback() {

          @Override public void onMovieLoaded(MovieDetail movieDetail) {
            checkNotNull(movieDetail);
            refreshCache(movieDetail);
            callback.onMovieLoaded(cachedMovieDetails.get(movieId));
          }

          @Override public void onDataNotAvailable() {
            callback.onDataNotAvailable();
          }
        }, movieId);
      }
    }, movieId);
  }

  private void getCreditsFromRemoteDataSource(@NonNull final FetchMovieCreditsCallback callback,
      int movieId) {

    remoteDataSource.getCredits(new FetchMovieCreditsCallback() {
      @Override public void onCreditsLoaded(Credits credits) {
        checkNotNull(credits);
        refreshCache(credits);
        localDataSource.saveCredits(credits);
        callback.onCreditsLoaded(cachedCredits.get(movieId));
      }

      @Override public void onDataNotAvailable() {
        callback.onDataNotAvailable();
        localDataSource.getCredits(new FetchMovieCreditsCallback() {

          @Override public void onCreditsLoaded(Credits credits) {
            checkNotNull(credits);
            refreshCache(credits);
            callback.onCreditsLoaded(cachedCredits.get(movieId));
          }

          @Override public void onDataNotAvailable() {
            callback.onDataNotAvailable();
          }
        }, movieId);
      }
    }, movieId);
  }

  @SuppressWarnings("Convert2streamapi") private void refreshCache(Upcoming upcomingMovies) {
    if (cachedUpcomingMovies == null) {
      cachedUpcomingMovies = new Upcoming();
    }
    for (Movie movie : upcomingMovies.getMovies()) {
      refreshCache(movie);
    }
    cacheIsDirty = false;
  }

  private void refreshCache(List<Genre> genres) {
    if (cachedGenres == null) {
      cachedGenres = new ArrayList<>();
    }
    this.cachedGenres = genres;
    cacheIsDirty = false;
  }

  private void refreshCache(MovieDetail movie) {
    if (cachedMovieDetails == null) {
      cachedMovieDetails = new SparseArray<>();
    }
    this.cachedMovieDetails.put(movie.getId(), movie);
    cacheIsDirty = false;
  }

  private void refreshCache(Movie movie) {
    if (cachedMovies == null) {
      cachedMovies = new SparseArray<>();
    }
    this.cachedMovies.put(movie.getId(), movie);
    cacheIsDirty = false;
  }

  private void refreshCache(Images images) {
    if (cachedImages == null) {
      cachedImages = new SparseArray<>();
    }
    this.cachedImages.put(images.getId(), images);
    cacheIsDirty = false;
  }

  private void refreshCache(Credits credits) {
    if (cachedCredits == null) {
      cachedCredits = new SparseArray<>();
    }
    this.cachedCredits.put(credits.getId(), credits);
    cacheIsDirty = false;
  }

  @Override public void clearSubscriptions() {
    remoteDataSource.clearSubscriptions();
  }
}