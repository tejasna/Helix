package com.helix;

import android.content.Context;
import com.helix.data.source.HelixApplicationScope;
import dagger.Module;
import dagger.Provides;

@Module public class ContextModule {

  private final Context context;

  ContextModule(Context context) {
    this.context = context;
  }

  @Provides @HelixApplicationScope public Context context() {
    return context;
  }
}
