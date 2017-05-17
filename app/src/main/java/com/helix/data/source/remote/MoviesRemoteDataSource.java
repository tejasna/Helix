package com.helix.data.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import com.helix.HelixApplication;
import com.helix.data.Credits;
import com.helix.data.Genre;
import com.helix.data.Genres;
import com.helix.data.MovieDetail;
import com.helix.data.Upcoming;
import com.helix.data.UpcomingGenreZip;
import com.helix.data.source.HelixApplicationScope;
import com.helix.data.source.MoviesDataSource;
import com.helix.data.source.MoviesRepository;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import timber.log.Timber;

import static com.helix.utils.PreConditions.checkNotNull;
import static io.reactivex.Observable.empty;

@HelixApplicationScope public class MoviesRemoteDataSource implements MoviesDataSource {

  private final MoviesApi restApi;

  private CompositeDisposable compositeSubscription;

  private Context context;

  private static final String API_KEY = "b7cd3340a794e5a2f35e3abb820b497f";

  private Genres cachedGenres;

  public MoviesRemoteDataSource(@NonNull Context context) {
    checkNotNull(context);
    this.context = context;
    this.restApi = ((HelixApplication) this.context).getRepositoryComponent().getMoviesApi();
    compositeSubscription = new CompositeDisposable();
  }

  @Override public void getUpcomingMovies(@NonNull FetchUpcomingMoviesCallback callback, int page) {

    Observable<Genres> genres;
    if (cachedGenres == null) {
      cachedGenres = new Genres();
      genres = restApi.getMovieGenres(API_KEY);
    } else {
      genres = Observable.just(cachedGenres);
    }

    Observable<Upcoming> movies = restApi.getUpcomingMovies(API_KEY, page);

    compositeSubscription.add(Observable.zip(movies, genres,
        (upcoming, genres1) -> new UpcomingGenreZip(upcoming, genres1.getGenres()))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> {
          Timber.e(throwable.getMessage());
          callback.onDataNotAvailable();
        })
        .onErrorResumeNext(throwable -> {
          callback.onDataNotAvailable();
          return empty();
        })
        .subscribe(upcomingGenreZip -> {
          cachedGenres.setGenres(upcomingGenreZip.getGenres());
          Timber.d(cachedGenres.getGenres().size() + "");
          callback.onMoviesLoaded(upcomingGenreZip.getUpcoming(), upcomingGenreZip.getGenres());
        }));

    /*Upcoming upcomingMovies = new Upcoming();
    List<MovieDetail> upcomingMovieDetailList = new ArrayList<>();

    compositeSubscription.add(restApi.getUpcomingMovies(API_KEY, page)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .onErrorResumeNext(throwable -> {
          callback.onDataNotAvailable();
          return empty();
        })
        .doOnNext(upcoming -> {
          upcomingMovies.setDates(upcoming.getDates());
          upcomingMovies.setMovies(upcoming.getMovies());
          upcomingMovies.setPage(upcoming.getPage());
          upcomingMovies.setTotalPages(upcoming.getTotalPages());
          upcomingMovies.setTotalResults(upcoming.getTotalResults());
        })
        .flatMapIterable(new Function<Upcoming, Iterable<Movie>>() {
          @Override public Iterable<Movie> apply(Upcoming upcoming) throws Exception {
            return upcoming.getMovies();
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .flatMap(movie -> restApi.getMovieDetail(movie.getId(), API_KEY))
        .doOnError(throwable -> {
          Timber.e(throwable.getMessage());
          callback.onDataNotAvailable();
        })
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(upcomingMovieDetailList::add)
        .doOnComplete(() -> {
          if (upcomingMovies.getMovies() != null) {
            callback.onMoviesLoaded(upcomingMovies, upcomingMovieDetailList);
          }
        })
        .subscribe());*/
  }

  @Override public void getMovieDetail(@NonNull FetchMovieDetailCallback callback, int movieId) {

    compositeSubscription.add(restApi.getMovieDetail(movieId, API_KEY)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> {
          Timber.e(throwable.getMessage());
          callback.onDataNotAvailable();
        })
        .onErrorResumeNext(throwable -> {
          callback.onDataNotAvailable();
          return empty();
        })
        .subscribe(callback::onMovieLoaded));
  }

  @Override public void getMovieBrief(@NonNull FetchMovieBriefCallback callback, int movieId) {
    callback.onDataNotAvailable();
  }

  @Override public void getImages(@NonNull FetchMovieImagesCallback callback, int movieId) {
    compositeSubscription.add(restApi.getMovieImages(movieId, API_KEY)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> {
          Timber.e(throwable.getMessage());
          callback.onDataNotAvailable();
        })
        .onErrorResumeNext(throwable -> {
          callback.onDataNotAvailable();
          return empty();
        })
        .subscribe(callback::onImagesLoaded));
  }

  @Override public void getCredits(@NonNull FetchMovieCreditsCallback callback, int movieId) {
    compositeSubscription.add(restApi.getMovieCredits(movieId, API_KEY)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> {
          Timber.e(throwable.getMessage());
          callback.onDataNotAvailable();
        })
        .onErrorResumeNext(throwable -> {
          callback.onDataNotAvailable();
          return empty();
        })
        .subscribe(callback::onCreditsLoaded));
  }

  @Override public void saveUpcomingMovies(@NonNull Upcoming upcomingMovies) {
    /**
     Not required because the {@link MoviesRepository} handles the logic of saving movies
     from all the available data sources.
     */
  }

  @Override public void saveUpcomingMovieDetails(@NonNull MovieDetail movie) {
    /**
     Not required because the {@link MoviesRepository} handles the logic of saving movie detail
     from all the available data sources.
     */
  }

  @Override public void saveGenres(@NonNull List<Genre> genres) {
    /**
     Not required because the {@link MoviesRepository} handles the logic of saving genres
     from all the available data sources.
     */
  }

  @Override public void saveCredits(@NonNull Credits credits) {
    /**
     Not required because the {@link MoviesRepository} handles the logic of saving credits
     from all the available data sources.
     */
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

  //@Override public void login(final @NonNull LoginCallback callback) {
  //  compositeSubscription.add(restApi.login()
  //      .subscribeOn(Schedulers.io())
  //      .observeOn(AndroidSchedulers.mainThread())
  //      .doOnError(throwable -> {
  //        Timber.e(throwable.getMessage());
  //        callback.onLoginFailure();
  //      })
  //      .onErrorResumeNext(throwable -> {
  //        callback.onLoginFailure();
  //        return empty();
  //      })
  //      .subscribe(login -> {
  //        WalletRemoteDataSource.this.handleLoginResponse(login, callback);
  //      }));
  //}
  //
  //@Override public void getTransactions(@NonNull LoadTransactionsCallback callback) {
  //  compositeSubscription.add(restApi.getTransactions(getAuthorizer())
  //      .subscribeOn(Schedulers.io())
  //      .observeOn(AndroidSchedulers.mainThread())
  //      .doOnError(throwable -> Timber.e(throwable.getMessage()))
  //      .onErrorResumeNext(throwable -> {
  //        callback.onDataNotAvailable();
  //        return empty();
  //      })
  //      .subscribe(callback::onMoviesLoaded));
  //}
  //
  //@Override public void saveTransactions(@NonNull List<Transaction> transactions) {
  //
  //}
  //
  //@Override
  //public void newTransaction(@NonNull Transaction transaction, SaveTransactionCallback callback) {
  //
  //  // Converts the balance in a transaction from native rate to GBP since the server accepts
  //  // transactions only in GBP but the client supports (currently 5) currencies
  //  double amountInGBP = CurrencyUtil.convertAmountToGBP(transaction);
  //
  //  transaction.setAmount(String.valueOf(amountInGBP));
  //
  //  compositeSubscription.add(restApi.spend(getAuthorizer(), transaction)
  //      .subscribeOn(Schedulers.io())
  //      .observeOn(AndroidSchedulers.mainThread())
  //      .doOnError(throwable -> Timber.e(throwable.getMessage()))
  //      .onErrorResumeNext(throwable -> {
  //        callback.onMoviesSaveFailed();
  //        return empty();
  //      })
  //      .subscribe(o -> {
  //        callback.onMoviesSaved();
  //      }));
  //}
  //
  //@Override public void getBalance(@NonNull LoadBalanceCallback callback) {
  //  compositeSubscription.add(restApi.getBalance(getAuthorizer())
  //      .subscribeOn(Schedulers.io())
  //      .observeOn(AndroidSchedulers.mainThread())
  //      .doOnError(throwable -> Timber.e(throwable.getMessage()))
  //      .onErrorResumeNext(throwable -> {
  //        callback.onDataNotAvailable();
  //        return empty();
  //      })
  //      .subscribe(balance -> {
  //        Realm.getDefaultInstance().executeTransaction(realm -> {
  //          Currency userPrefCurrency = realm.copyFromRealm(
  //              realm.where(Currency.class).equalTo("userPref", true).findFirst());
  //          realm.close();
  //          callback.onBalanceLoaded(balance, userPrefCurrency);
  //        });
  //      }));
  //}
  //
  //@Override public void saveBalance(@NonNull Balance balance) {
  //
  //}
  //
  //@Override public void getCurrencies(@NonNull LoadCurrenciesCallback callback) {
  //  String json = loadJSONFromAsset(context);
  //  if (json == null) {
  //    callback.onDataNotAvailable();
  //  } else {
  //    Realm.getDefaultInstance().executeTransaction(realm -> {
  //      realm.createOrUpdateAllFromJson(Currency.class, json);
  //      realm.close();
  //    });
  //
  //    Realm.getDefaultInstance().executeTransaction(realm -> {
  //      RealmResults<Currency> currencies = realm.where(Currency.class).findAll();
  //      callback.onCurrencyLoaded(realm.copyFromRealm(currencies));
  //      realm.close();
  //    });
  //  }
  //}
  //
  //@Override public void saveCurrencies(@NonNull List<Currency> currencies) {
  //
  //}
  //
  //@Override public void getPreferredCurrency(@NonNull LoadCurrenciesCallback callback) {
  //
  //}
  //
  //@Override public void savePreferredCurrency(@NonNull Currency currency) {
  //
  //}
  //
  //@Override
  //public void isBalanceGreaterThan(@NonNull BalanceAvailabilityCallback callback, double amount) {
  //  compositeSubscription.add(restApi.getBalance(getAuthorizer())
  //      .subscribeOn(Schedulers.io())
  //      .observeOn(AndroidSchedulers.mainThread())
  //      .doOnError(throwable -> Timber.e(throwable.getMessage()))
  //      .onErrorResumeNext(throwable -> {
  //        callback.onBalanceAvailabilityError();
  //        return empty();
  //      })
  //      .subscribe(balance -> {
  //        if (Integer.parseInt(balance.getBalance()) > amount) {
  //          callback.onBalanceSufficient();
  //        } else {
  //          callback.onBalanceInsufficient();
  //        }
  //      }));
  //}
  //
  //private void handleLoginResponse(Login login, LoginCallback callback) {
  //  Timber.d(login.getToken());
  //  callback.onLoginSuccess(login);
  //}
  //
  //@Override public void checkLoginState(@NonNull LoginCallback callback) {
  //  // Not required for the remote data source because the {@link TasksRepository} handles
  //}
  //
  //@Override public void saveLoginState(@NonNull Login login) {
  //  // Not required for the remote data source because the {@link TasksRepository} handles
  //}
  //
  //@Override public void clearLoginState() {
  //  // Not required for the remote data source because the {@link TasksRepository} handles
  //}
  //
  //@Override public void clearSubscriptions() {
  //  compositeSubscription.clear();
  //}
  //
  //@Override public void logout() {
  //
  //}
  //
  //@Override public void refreshTransactions() {
  //  // Not required because the {@link TransactionsRepository} handles the logic of refreshing the
  //  // transactions from all the available data sources.
  //}
  //
  //@Override public void deleteAllTransactions() {
  //  //TASKS_SERVICE_DATA.clear();
  //}
  //
  //@Override public void deleteExistingBalance() {
  //
  //}
  //
  //
  //private String loadJSONFromAsset(Context context) {
  //  String json;
  //  try {
  //    InputStream is = context.getAssets().open("currencies.json");
  //    int size = is.available();
  //    byte[] buffer = new byte[size];
  //    //noinspection ResultOfMethodCallIgnored
  //    is.read(buffer);
  //    is.close();
  //    json = new String(buffer, "UTF-8");
  //  } catch (IOException ex) {
  //    ex.printStackTrace();
  //    return null;
  //  }
  //  return json;
  //}
}