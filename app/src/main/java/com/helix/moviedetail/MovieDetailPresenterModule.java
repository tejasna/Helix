package com.helix.moviedetail;

import dagger.Module;
import dagger.Provides;

@Module class MovieDetailPresenterModule {

  private final MovieDetailContract.View movieDetailView;

  public MovieDetailPresenterModule(MovieDetailContract.View movieDetailView) {
    this.movieDetailView = movieDetailView;
  }

  @Provides MovieDetailContract.View provideMovieDetailContractView() {
    return movieDetailView;
  }
}
