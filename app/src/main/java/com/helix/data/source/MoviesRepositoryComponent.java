package com.helix.data.source;

import com.helix.data.source.remote.MoviesApi;
import com.squareup.picasso.Picasso;
import dagger.Component;

@HelixApplicationScope
@Component(modules = { MoviesRepositoryModule.class, MoviesModule.class, PicassoModule.class })
public interface MoviesRepositoryComponent {

  MoviesRepository getMoviesRepository();

  MoviesApi getMoviesApi();

  Picasso getPicasso();
}

