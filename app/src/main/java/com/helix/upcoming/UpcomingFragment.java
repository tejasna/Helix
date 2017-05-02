package com.helix.upcoming;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.helix.R;

import static com.helix.utils.PreConditions.checkNotNull;

public class UpcomingFragment extends Fragment implements UpcomingContract.View {

  private UpcomingContract.Presenter presenter;

  private Unbinder unbinder;

  public UpcomingFragment() {
  }

  public static UpcomingFragment newInstance() {
    return new UpcomingFragment();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.upcoming_frag, container, false);

    unbinder = ButterKnife.bind(this, rootView);

    return rootView;
  }

  @Override public void setPresenter(UpcomingContract.Presenter presenter) {
    this.presenter = checkNotNull(presenter);
  }

  @Override public void onResume() {
    super.onResume();
    presenter.start();
  }

  @Override public void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
    presenter.stop();
  }
}
