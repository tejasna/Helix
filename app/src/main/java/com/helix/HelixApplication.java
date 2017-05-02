package com.helix;

import android.app.Application;
import com.helix.data.source.DaggerMoviesRepositoryComponent;
import com.helix.data.source.MoviesRepositoryComponent;
import timber.log.Timber;

public class HelixApplication extends Application {

  private MoviesRepositoryComponent repositoryComponent;

  @Override public void onCreate() {
    super.onCreate();

    Timber.plant(new Timber.DebugTree());

    repositoryComponent =
        DaggerMoviesRepositoryComponent.builder().contextModule(new ContextModule(this)).build();
  }

  public MoviesRepositoryComponent getRepositoryComponent() {
    return repositoryComponent;
  }
}
