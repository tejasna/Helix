package com.helix.data.source;

import java.util.List;

public interface MoviesDataSource {

  interface LoadMoviesCallback {

    void onMoviesLoaded(List<Object> transactions);

    void onDataNotAvailable();
  }

  interface SaveMoviesCallback {

    void onMoviesSaved();

    void onMoviesSaveFailed();
  }
}

//  void login(@NonNull LoginCallback callback);
//
//  void checkLoginState(@NonNull LoginCallback callback);
//
//  void saveLoginState(@NonNull Login login);
//
//
//  void getTransactions(@NonNull LoadTransactionsCallback callback);
//
//  void saveTransactions(@NonNull List<Transaction> transactions);
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
//  void refreshTransactions();
//
//  void deleteAllTransactions();
//
//  void deleteExistingBalance();
//
//  void clearSubscriptions();
//
//  void logout();
//}
