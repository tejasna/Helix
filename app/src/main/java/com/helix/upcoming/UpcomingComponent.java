package com.helix.upcoming;

import com.helix.data.source.MoviesRepositoryComponent;
import com.helix.utils.FragmentScoped;
import dagger.Component;

@FragmentScoped
@Component(dependencies = MoviesRepositoryComponent.class, modules = UpcomingPresenterModule.class)
interface UpcomingComponent {

  void inject(UpcomingActivity activity);
}
