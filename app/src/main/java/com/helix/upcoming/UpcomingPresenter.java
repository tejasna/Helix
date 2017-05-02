package com.helix.upcoming;

import com.helix.data.source.MoviesRepository;
import javax.inject.Inject;

public class UpcomingPresenter implements UpcomingContract.Presenter {

  private final MoviesRepository repository;

  private final UpcomingContract.View upcomingView;

  @Inject UpcomingPresenter(MoviesRepository tasksRepository, UpcomingContract.View loginView) {
    repository = tasksRepository;
    this.upcomingView = loginView;
  }

  @Inject void setupListeners() {
    upcomingView.setPresenter(this);
  }

  @Override public void start() {

  }

  @Override public void stop() {
    repository.clearSubscriptions();
  }
}
