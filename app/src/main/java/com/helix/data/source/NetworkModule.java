package com.helix.data.source;

import android.content.Context;
import com.helix.ContextModule;
import dagger.Module;
import dagger.Provides;
import java.io.File;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

@Module(includes = ContextModule.class) public class NetworkModule {

  @Provides @HelixApplicationScope File cacheFile(Context context) {
    return new File(context.getCacheDir(), "okhttp_cache");
  }

  @Provides @HelixApplicationScope Cache cache(File cacheFile) {
    return new Cache(cacheFile, 10 * 1000 * 1000); // 10MB
  }

  @Provides @HelixApplicationScope HttpLoggingInterceptor loggingInterceptor() {

    HttpLoggingInterceptor interceptor;
    interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
      @Override public void log(String message) {
        Timber.i(message);
      }
    });

    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    return interceptor;
  }

  @Provides @HelixApplicationScope OkHttpClient okHttpClient(
      HttpLoggingInterceptor loggingInterceptor, Cache cache) {
    return new OkHttpClient.Builder().addInterceptor(loggingInterceptor).cache(cache).build();
  }
}
