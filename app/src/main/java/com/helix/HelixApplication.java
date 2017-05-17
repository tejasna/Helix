package com.helix;

import android.app.Application;
import com.helix.data.source.DaggerMoviesRepositoryComponent;
import com.helix.data.source.MoviesRepositoryComponent;
import com.helix.widget.FontCache;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

public class HelixApplication extends Application {

  private MoviesRepositoryComponent repositoryComponent;

  public HelixApplication helixApplication;

  @Override public void onCreate() {
    super.onCreate();

    Timber.plant(new Timber.DebugTree());

    Realm.init(this);

    RealmConfiguration realmConfiguration =
        new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();

    Realm.setDefaultConfiguration(realmConfiguration);

    FontCache.init(this);

    helixApplication = this;

    repositoryComponent =
        DaggerMoviesRepositoryComponent.builder().contextModule(new ContextModule(this)).build();
  }

  public MoviesRepositoryComponent getRepositoryComponent() {
    return repositoryComponent;
  }
}
