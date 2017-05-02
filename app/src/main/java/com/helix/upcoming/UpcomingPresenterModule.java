package com.helix.upcoming;

import dagger.Module;
import dagger.Provides;

@Module class UpcomingPresenterModule {

  private final UpcomingContract.View upcomingView;

  public UpcomingPresenterModule(UpcomingContract.View upcomingView) {
    this.upcomingView = upcomingView;
  }

  @Provides UpcomingContract.View provideUpcomingContractView() {
    return upcomingView;
  }
}
