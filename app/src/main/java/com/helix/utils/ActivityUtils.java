package com.helix.utils;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import static com.helix.utils.PreConditions.checkNotNull;

public class ActivityUtils {

  public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
      @NonNull Fragment fragment, int frameId) {
    checkNotNull(fragmentManager);
    checkNotNull(fragment);
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.add(frameId, fragment);
    transaction.commit();
  }

  public static void showSnackBar(@NonNull String message, @NonNull View view) {
    checkNotNull(message);
    checkNotNull(view);
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
  }
}
