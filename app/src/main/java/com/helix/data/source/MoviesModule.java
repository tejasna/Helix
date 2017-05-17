package com.helix.data.source;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.helix.data.RealmInt;
import com.helix.data.RealmString;
import com.helix.data.source.remote.MoviesApi;
import dagger.Module;
import dagger.Provides;
import io.realm.RealmList;
import io.realm.RealmObject;
import java.io.IOException;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = NetworkModule.class) class MoviesModule {

  private static final String BASE_URL = "https://api.themoviedb.org/";

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
        .registerTypeAdapter(new TypeToken<RealmList<RealmInt>>() {
        }.getType(), new TypeAdapter<RealmList<RealmInt>>() {

          @Override public void write(JsonWriter out, RealmList<RealmInt> value)
              throws IOException {
            // Ignore
          }

          @Override public RealmList<RealmInt> read(JsonReader in) throws IOException {
            RealmList<RealmInt> list = new RealmList<RealmInt>();
            in.beginArray();
            while (in.hasNext()) {
              list.add(new RealmInt(in.nextInt()));
            }
            in.endArray();
            return list;
          }
        })
        .registerTypeAdapter(new TypeToken<RealmList<RealmString>>() {
        }.getType(), new TypeAdapter<RealmList<RealmString>>() {

          @Override public void write(JsonWriter out, RealmList<RealmString> value)
              throws IOException {
            // Ignore
          }

          @Override public RealmList<RealmString> read(JsonReader in) throws IOException {
            RealmList<RealmString> list = new RealmList<RealmString>();
            in.beginArray();
            while (in.hasNext()) {
              list.add(new RealmString(in.nextString()));
            }
            in.endArray();
            return list;
          }
        })
        .create();

    return gsonBuilder.create();
  }
}
