package com.helix.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;
import com.helix.data.Movie;
import com.helix.data.MovieDetail;
import com.helix.data.Upcoming;
import com.helix.data.source.HelixApplicationScope;
import com.helix.data.source.MovieCollection;
import com.helix.data.source.MoviesDataSource;
import com.helix.data.source.MoviesRepository;
import io.realm.Realm;
import io.realm.RealmList;
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

  @SuppressWarnings("NullableProblems") @Override
  public void getUpcomingMovies(@NonNull FetchUpcomingMoviesCallback callback, @NonNull int page) {

    RealmResults<Upcoming> upcoming = Realm.getDefaultInstance().where(Upcoming.class).findAll();

    RealmResults<MovieDetail> movieDetail =
        Realm.getDefaultInstance().where(MovieDetail.class).findAll();

    if (upcoming == null || movieDetail == null) {
      callback.onDataNotAvailable();
      return;
    }

    Observable<RealmResults<Upcoming>> upcomingObservable = upcoming.asObservable();

    Observable<RealmResults<MovieDetail>> upcomingDetailsObservable = movieDetail.asObservable();

    compositeSubscription.add(
        Observable.zip(upcomingObservable, upcomingDetailsObservable, MovieCollection::new)
            .doOnError(throwable -> {
              Timber.e(throwable.getMessage());
              callback.onDataNotAvailable();
            })
            .onErrorResumeNext(throwable -> {
              return Observable.empty();
            })
            .map(movieCollection -> {
              RealmList<Upcoming> upcomingMovies = new RealmList<>();
              RealmList<Movie> paginatedList = new RealmList<>();
              Realm realm = Realm.getDefaultInstance();
              realm.beginTransaction();
              upcomingMovies.addAll(movieCollection.getUpcoming());
              paginatedList.addAll(upcomingMovies.get(page).getMovies());
              upcomingMovies.get(page).setMovies(paginatedList);
              realm.commitTransaction();

              return movieCollection;
            })
            .subscribe(movieCollection -> {
              callback.onMoviesLoaded(movieCollection.getUpcoming().get(page),
                  movieCollection.getMovieDetails());
            }, throwable -> {
              Timber.e(throwable.getMessage());
              callback.onDataNotAvailable();
            }));
  }

  @Override public void saveUpcomingMovies(@NonNull Upcoming upcomingMovies) {
    checkNotNull(upcomingMovies);
    Realm.getDefaultInstance().executeTransactionAsync(realm -> {
      realm.copyToRealmOrUpdate(upcomingMovies);
      realm.commitTransaction();
      realm.close();
    });
  }

  @Override public void saveUpcomingMovieDetails(@NonNull List<MovieDetail> upcomingMovieDetail) {
    checkNotNull(upcomingMovieDetail);
    Realm.getDefaultInstance().executeTransactionAsync(realm -> {
      realm.copyToRealmOrUpdate(upcomingMovieDetail);
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

  @Override public void clearSubscriptions() {
    compositeSubscription.clear();
  }
}