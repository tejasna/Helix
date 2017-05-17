package com.helix.utils;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

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

  public static void circularRevealShow(View view) {

    int x = view.getWidth() / 2;
    int y = view.getHeight() / 2;

    int endRadius = (int) Math.hypot(view.getWidth(), view.getHeight());

    Animator anim = ViewAnimationUtils.createCircularReveal(view, x, y, 0, endRadius);
    anim.setDuration(400);

    anim.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animator) {
        view.setVisibility(View.VISIBLE);
      }

      @Override public void onAnimationEnd(Animator animator) {
      }

      @Override public void onAnimationCancel(Animator animator) {

      }

      @Override public void onAnimationRepeat(Animator animator) {

      }
    });

    anim.start();
  }

  public static void circularRevealHide(View view) {

    int x = view.getRight() / 2;
    int y = view.getBottom() / 2;

    int hypotenuse = (int) Math.hypot(view.getWidth(), view.getHeight());
    //
    //FrameLayout.LayoutParams parameters = (FrameLayout.LayoutParams) revealView.getLayoutParams();
    //parameters.height = view.getHeight();
    //revealView.setLayoutParams(parameters);

    Animator anim = ViewAnimationUtils.createCircularReveal(view, x, y, hypotenuse, 0);
    anim.setDuration(400);

    anim.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animator) {

      }

      @Override public void onAnimationEnd(Animator animator) {
        view.setVisibility(View.INVISIBLE);
      }

      @Override public void onAnimationCancel(Animator animator) {

      }

      @Override public void onAnimationRepeat(Animator animator) {

      }
    });

    anim.start();
  }

  public static void animate(View view) {

    TransitionManager.beginDelayedTransition((ViewGroup) view.getParent());
    view.setVisibility(View.VISIBLE);
  }
}
