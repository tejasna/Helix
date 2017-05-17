package com.helix.data.source;

import android.support.annotation.NonNull;
import com.helix.data.Credits;
import com.helix.data.Genre;
import com.helix.data.Images;
import com.helix.data.Movie;
import com.helix.data.MovieDetail;
import com.helix.data.Upcoming;
import java.util.List;

public interface MoviesDataSource {

  interface FetchUpcomingMoviesCallback {

    void onMoviesLoaded(Upcoming upcomingMovies, List<Genre> genres);

    void onDataNotAvailable();
  }

  interface FetchMovieDetailCallback {

    void onMovieLoaded(MovieDetail movieDetail);

    void onDataNotAvailable();
  }

  interface FetchMovieBriefCallback {

    void onMovieLoaded(Movie movie);

    void onDataNotAvailable();
  }

  interface FetchMovieImagesCallback {

    void onImagesLoaded(Images images);

    void onDataNotAvailable();
  }

  interface FetchMovieCreditsCallback {

    void onCreditsLoaded(Credits credits);

    void onDataNotAvailable();
  }

  void getUpcomingMovies(@NonNull FetchUpcomingMoviesCallback callback, int page);

  void saveUpcomingMovies(@NonNull Upcoming upcomingMovies);

  void saveUpcomingMovieDetails(@NonNull MovieDetail movie);

  void saveGenres(@NonNull List<Genre> genres);

  void saveCredits(@NonNull Credits credits);

  void getMovieDetail(@NonNull FetchMovieDetailCallback callback, int movieId);

  void getMovieBrief(@NonNull FetchMovieBriefCallback callback, int movieId);

  void getImages(@NonNull FetchMovieImagesCallback callback, int movieId);

  void getCredits(@NonNull FetchMovieCreditsCallback callback, int movieId);

  void refreshMovies();

  void clearSubscriptions();
}
