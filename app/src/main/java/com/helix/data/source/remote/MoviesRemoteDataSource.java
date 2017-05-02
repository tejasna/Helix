package com.helix.data.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import com.helix.data.source.HelixApplicationScope;
import com.helix.data.source.MoviesDataSource;
import io.reactivex.disposables.CompositeDisposable;

import static com.helix.utils.PreConditions.checkNotNull;

@HelixApplicationScope public class MoviesRemoteDataSource implements MoviesDataSource {

  //private final MoviesApi restApi;

  private CompositeDisposable compositeSubscription;

  private Context context;

  public MoviesRemoteDataSource(@NonNull Context context) {
    checkNotNull(context);
    this.context = context;
    compositeSubscription = new CompositeDisposable();
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
  //private String getAuthorizer() {
  //  String bearer = Realm.getDefaultInstance().where(Login.class).findFirst().getToken();
  //  return authPrefix + bearer;
  //}
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