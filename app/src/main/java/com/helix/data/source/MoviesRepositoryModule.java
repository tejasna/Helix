package com.helix.data.source;

import android.content.Context;
import com.helix.data.source.local.MoviesLocalDataSource;
import com.helix.data.source.remote.MoviesRemoteDataSource;
import dagger.Module;
import dagger.Provides;

@Module(includes = NetworkModule.class) class MoviesRepositoryModule {

  @HelixApplicationScope @Provides @Local MoviesDataSource provideMoviesLocalDataSource(
      Context context) {
    return new MoviesLocalDataSource(context);
  }

  @HelixApplicationScope @Provides @Remote MoviesDataSource provideMoviesRemoteDataSource(
      Context context) {
    return new MoviesRemoteDataSource(context);
  }
}

