package com.helix.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;
import com.helix.data.Credits;
import com.helix.data.Genre;
import com.helix.data.Movie;
import com.helix.data.MovieDetail;
import com.helix.data.Upcoming;
import com.helix.data.UpcomingGenreZip;
import com.helix.data.source.HelixApplicationScope;
import com.helix.data.source.MoviesDataSource;
import com.helix.data.source.MoviesRepository;
import io.realm.Realm;
import io.realm.RealmResults;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static com.helix.utils.PreConditions.checkNotNull;

@HelixApplicationScope public class MoviesLocalDataSource implements MoviesDataSource {

  private CompositeSubscription compositeSubscription;

  @Inject public MoviesLocalDataSource(@NonNull Context context) {
    checkNotNull(context);
    compositeSubscription = new CompositeSubscription();
  }

  @Override public void getUpcomingMovies(@NonNull FetchUpcomingMoviesCallback callback, int page) {

    Upcoming upcomingCache =
        Realm.getDefaultInstance().where(Upcoming.class).equalTo("page", page).findFirst();
    RealmResults<Genre> genreCache = Realm.getDefaultInstance().where(Genre.class).findAll();

    if (upcomingCache == null || genreCache == null) {
      callback.onDataNotAvailable();
      return;
    }

    Observable<Upcoming> upcoming = upcomingCache.asObservable();
    Observable<RealmResults<Genre>> genres = genreCache.asObservable();

    compositeSubscription.add(
        Observable.zip(upcoming, genres, UpcomingGenreZip::new).doOnError(throwable -> {
          Timber.e(throwable.getMessage());
          callback.onDataNotAvailable();
        }).onErrorResumeNext(throwable -> {
          return Observable.empty();
        }).subscribe(upcomingGenreZip -> {
          callback.onMoviesLoaded(upcomingGenreZip.getUpcoming(), upcomingGenreZip.getGenres());
        }));
  }

  @Override public void getMovieDetail(@NonNull FetchMovieDetailCallback callback, int movieId) {

    MovieDetail movieDetailCache =
        Realm.getDefaultInstance().where(MovieDetail.class).equalTo("id", movieId).findFirst();

    if (movieDetailCache == null) {
      callback.onDataNotAvailable();
      return;
    }

    Observable<MovieDetail> movie = movieDetailCache.asObservable();

    compositeSubscription.add(movie.doOnError(throwable -> {
      Timber.e(throwable.getMessage());
      callback.onDataNotAvailable();
    }).onErrorResumeNext(throwable -> {
      return Observable.empty();
    }).subscribe(callback::onMovieLoaded));
  }

  @Override public void getMovieBrief(@NonNull FetchMovieBriefCallback callback, int movieId) {

    Movie movieCache =
        Realm.getDefaultInstance().where(Movie.class).equalTo("id", movieId).findFirst();

    if (movieCache == null) {
      callback.onDataNotAvailable();
      return;
    }

    Observable<Movie> movie = movieCache.asObservable();

    compositeSubscription.add(movie.doOnError(throwable -> {
      Timber.e(throwable.getMessage());
      callback.onDataNotAvailable();
    }).onErrorResumeNext(throwable -> {
      return Observable.empty();
    }).subscribe(callback::onMovieLoaded));
  }

  @Override public void getCredits(@NonNull FetchMovieCreditsCallback callback, int movieId) {

    Credits credits =
        Realm.getDefaultInstance().where(Credits.class).equalTo("id", movieId).findFirst();

    if (credits == null) {
      callback.onDataNotAvailable();
      return;
    }

    Observable<Credits> credit = credits.asObservable();

    compositeSubscription.add(credit.doOnError(throwable -> {
      Timber.e(throwable.getMessage());
      callback.onDataNotAvailable();
    }).onErrorResumeNext(throwable -> {
      return Observable.empty();
    }).subscribe(callback::onCreditsLoaded));
  }

  @Override public void saveUpcomingMovies(@NonNull Upcoming upcomingMovies) {
    checkNotNull(upcomingMovies);
    Realm.getDefaultInstance().executeTransactionAsync(realm -> {
      realm.copyToRealmOrUpdate(upcomingMovies);
      realm.commitTransaction();
      realm.close();
    });
  }

  @Override public void saveUpcomingMovieDetails(@NonNull MovieDetail movie) {
    checkNotNull(movie);
    Realm.getDefaultInstance().executeTransactionAsync(realm -> {
      realm.copyToRealmOrUpdate(movie);
      realm.commitTransaction();
      realm.close();
    });
  }

  @Override public void saveGenres(@NonNull List<Genre> genres) {
    checkNotNull(genres);
    Realm.getDefaultInstance().executeTransactionAsync(realm -> {
      realm.copyToRealmOrUpdate(genres);
      realm.commitTransaction();
      realm.close();
    });
  }

  @Override public void saveCredits(@NonNull Credits credits) {
    checkNotNull(credits);
    Realm.getDefaultInstance().executeTransactionAsync(realm -> {
      realm.copyToRealmOrUpdate(credits);
      realm.commitTransaction();
      realm.close();
    });
  }

  @Override public void refreshMovies() {
    /**
     Not required because the {@link MoviesRepository} handles the logic of refreshing the movies
     from all the available data sources.
     */
  }

  @Override public void getImages(@NonNull FetchMovieImagesCallback callback, int movieId) {
    /**
     Not required because the {@link MoviesRepository} does not cache images in memory or in DB.
     */
  }

  @Override public void clearSubscriptions() {
    compositeSubscription.clear();
  }
}