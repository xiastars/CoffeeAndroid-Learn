package com.summer.demo.ui.view.customfragment.text;

import android.content.Context;
import android.os.Build;
import android.text.Layout;
import android.util.AttributeSet;

public class MyJustifiedTextView extends JustifyTextView {

  public MyJustifiedTextView(final  Context context, final AttributeSet attrs) {
    super(context, attrs);
    if (!isInEditMode()) {

    }
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    // setTextIsSelectable doesn't work unless the text view is attached to the window
    // because it uses the window layout params to check if it can display the handles.
    if (Build.VERSION.SDK_INT > 10) {
     //setTextIsSelectable(true);
    }
  }


  // We want our text to be selectable, but we still want links to be clickable.



  private int getLineAtCoordinate( Layout layout, final float y) {
    final int max = getHeight() - getTotalPaddingBottom() - 1;
    final int v = Math.min(max, Math.max(0, (int)y - getTotalPaddingTop())) + getScrollY();
    return layout.getLineForVertical(v);
  }

  private int getOffsetAtCoordinate( Layout layout, final int line, final float x) {
    final int  max = getWidth() - getTotalPaddingRight() - 1;
    final int v = Math.min(max, Math.max(0, (int)x - getTotalPaddingLeft())) + getScrollX();
    return layout.getOffsetForHorizontal(line, v);
  }

}