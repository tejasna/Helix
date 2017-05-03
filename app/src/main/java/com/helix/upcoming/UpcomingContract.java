package com.helix.upcoming;

import com.helix.BasePresenter;
import com.helix.BaseView;
import com.helix.data.Movie;
import java.util.List;

class UpcomingContract {

  interface View extends BaseView<Presenter> {

    void setLoadingIndicator(boolean active);

    void showMovies(List<Movie> movies);

    void showNoMovies();

    void showLoadingMoviesError();

    boolean isActive();
  }

  interface Presenter extends BasePresenter {

    void loadMovies(boolean forceUpdate);

    void loadMore(int page);
  }
}
