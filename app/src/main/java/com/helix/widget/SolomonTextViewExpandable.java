package com.helix.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import com.helix.R;

public class SolomonTextViewExpandable extends AppCompatTextView implements View.OnClickListener {
  private static final int MAX_LINES = 5;
  private int currentMaxLines = Integer.MAX_VALUE;

  public SolomonTextViewExpandable(Context context) {
    super(context);
    init();
  }

  public SolomonTextViewExpandable(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  public SolomonTextViewExpandable(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    setTypeface(FontCache.regular);
    setOnClickListener(this);
  }

  @Override
  protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
    post(() -> {
      if (getLineCount() > MAX_LINES) {
        setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_more);
      } else {
        setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
      }

      setMaxLines(MAX_LINES);
    });
  }

  @Override public void setMaxLines(int maxLines) {
    currentMaxLines = maxLines;
    super.setMaxLines(maxLines);
  }

  public int getMyMaxLines() {
    return currentMaxLines;
  }

  @Override public void onClick(View v) {
    if (getMyMaxLines() == Integer.MAX_VALUE) {
      setMaxLines(MAX_LINES);
    } else {
      setMaxLines(Integer.MAX_VALUE);
    }
  }
}