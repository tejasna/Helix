package com.helix.upcoming;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.helix.HelixApplication;
import com.helix.R;
import com.helix.moviedetail.MovieDetailActivity;
import com.helix.utils.ActivityUtils;
import com.helix.utils.ClickDelegate;
import javax.inject.Inject;

public class UpcomingActivity extends AppCompatActivity implements ClickDelegate {

  @Inject UpcomingPresenter presenter;

  @BindView(R.id.toolbar) Toolbar toolbar;

  private Unbinder unbinder;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.upcoming_act);

    unbinder = ButterKnife.bind(this);

    setSupportActionBar(toolbar);

    UpcomingFragment upcomingFragment =
        (UpcomingFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);

    if (upcomingFragment == null) {
      upcomingFragment = UpcomingFragment.newInstance(this);
      ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), upcomingFragment,
          R.id.content_frame);
    }

    DaggerUpcomingComponent.builder()
        .moviesRepositoryComponent(((HelixApplication) getApplication()).getRepositoryComponent())
        .upcomingPresenterModule(new UpcomingPresenterModule(upcomingFragment))
        .build()
        .inject(this);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    presenter.stop();
    unbinder.unbind();
  }

  @Override public void clicked(int movieId, String posterUrl, View clickedView) {
    ActivityOptionsCompat options = ActivityOptionsCompat.
        makeSceneTransitionAnimation(this, clickedView, getString(R.string.shared_poster_element));

    Intent intent = new Intent(this, MovieDetailActivity.class);
    intent.putExtra("movie_id", movieId);
    intent.putExtra("poster_url", posterUrl);
    startActivity(intent, options.toBundle());
  }
}
