package com.helix.moviedetail;

import com.helix.data.source.MoviesRepositoryComponent;
import com.helix.utils.FragmentScoped;
import dagger.Component;

@FragmentScoped
@Component(dependencies = MoviesRepositoryComponent.class, modules = MovieDetailPresenterModule.class)
interface MovieDetailComponent {

  void inject(MovieDetailActivity activity);

}
