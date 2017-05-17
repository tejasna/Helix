package com.helix.data.source;

import android.content.Context;
import com.helix.ContextModule;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module(includes = { ContextModule.class, NetworkModule.class }) class PicassoModule {

  @Provides @HelixApplicationScope Picasso picasso(Context context,
      OkHttp3Downloader okHttp3Downloader) {
    return new Picasso.Builder(context).downloader(okHttp3Downloader).build();
  }

  @Provides @HelixApplicationScope OkHttp3Downloader okHttp3Downloader(OkHttpClient okHttpClient) {
    return new OkHttp3Downloader(okHttpClient);
  }
}
