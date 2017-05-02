package com.helix.data.source;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.helix.data.source.remote.MoviesApi;
import dagger.Module;
import dagger.Provides;
import io.realm.RealmObject;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = NetworkModule.class) public class MoviesModule {

  private static final String BASE_URL = "";

  @Provides @HelixApplicationScope MoviesApi moviesApi(Retrofit tmdbRetrofit) {
    return tmdbRetrofit.create(MoviesApi.class);
  }

  @Provides @HelixApplicationScope Retrofit retrofit(OkHttpClient okHttpClient, Gson gson) {
    return new Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build();
  }

  @Provides @HelixApplicationScope Gson gson() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.excludeFieldsWithoutExposeAnnotation()
        .setExclusionStrategies(new ExclusionStrategy() {
          @Override public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaringClass().equals(RealmObject.class);
          }

          @Override public boolean shouldSkipClass(Class<?> clazz) {
            return false;
          }
        })
        .create();

    return gsonBuilder.create();
  }
}
