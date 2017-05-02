package com.helix.data.source;

import dagger.Component;

@HelixApplicationScope @Component(modules = { MoviesRepositoryModule.class, MoviesModule.class })
public interface MoviesRepositoryComponent {

  MoviesRepository getMoviesRepository();
}

