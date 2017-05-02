package com.helix.upcoming;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.helix.HelixApplication;
import com.helix.R;
import com.helix.utils.ActivityUtils;
import javax.inject.Inject;

public class UpcomingActivity extends AppCompatActivity {

  @Inject UpcomingPresenter presenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.upcoming_act);

    UpcomingFragment upcomingFragment =
        (UpcomingFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);

    if (upcomingFragment == null) {
      upcomingFragment = UpcomingFragment.newInstance();
      ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), upcomingFragment,
          R.id.content_frame);
    }

    DaggerUpcomingComponent.builder()
        .moviesRepositoryComponent(((HelixApplication) getApplication()).getRepositoryComponent())
        .upcomingPresenterModule(new UpcomingPresenterModule(upcomingFragment))
        .build()
        .inject(this);
  }
}
