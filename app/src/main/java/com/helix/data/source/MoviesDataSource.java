package com.helix.data.source;

import android.support.annotation.NonNull;
import com.helix.data.MovieDetail;
import com.helix.data.Upcoming;
import java.util.List;

public interface MoviesDataSource {

  interface FetchUpcomingMoviesCallback {

    void onMoviesLoaded(Upcoming upcomingMovies, List<MovieDetail> upcomingMovieDetail);

    void onDataNotAvailable();
  }

  interface SaveMoviesCallback {

    void onMoviesSaved();

    void onMoviesSaveFailed();
  }

  //  void login(@NonNull LoginCallback callback);
  //
  //  void checkLoginState(@NonNull LoginCallback callback);
  //
  //  void saveLoginState(@NonNull Login login);
  //

  void getUpcomingMovies(@NonNull FetchUpcomingMoviesCallback callback, @NonNull int page);

  void saveUpcomingMovies(@NonNull Upcoming upcomingMovies);

  void saveUpcomingMovieDetails(@NonNull List<MovieDetail> upcomingMovieDetail);

  //
  //  void newTransaction(@NonNull Transaction transaction, SaveTransactionCallback callback);
  //
  //  void getBalance(@NonNull LoadBalanceCallback callback);
  //
  //  void saveBalance(@NonNull Balance balance);
  //
  //  void getCurrencies(@NonNull LoadCurrenciesCallback callback);
  //
  //  void saveCurrencies(@NonNull List<Currency> currencies);
  //
  //  void getPreferredCurrency(@NonNull LoadCurrenciesCallback callback);
  //
  //  void savePreferredCurrency(@NonNull Currency currency);
  //
  //  void isBalanceGreaterThan(@NonNull BalanceAvailabilityCallback callback, double amount);
  //
  void refreshMovies();

  //
  //  void deleteAllTransactions();
  //
  //  void deleteExistingBalance();
  //
  void clearSubscriptions();
  //
  //  void logout();
  //}
}
