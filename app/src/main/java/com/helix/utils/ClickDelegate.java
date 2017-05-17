package com.helix.utils;

import android.view.View;

public interface ClickDelegate {

  void clicked(int movieId, String posterUrl, View clickedView);
}
