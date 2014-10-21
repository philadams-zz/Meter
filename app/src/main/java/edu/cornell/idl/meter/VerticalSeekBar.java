package edu.cornell.idl.meter;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class VerticalSeekBar extends SeekBar {

  public VerticalSeekBar(Context context) {
    super(context);
  }

  public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public VerticalSeekBar(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
    super.onSizeChanged(height, width, oldHeight, oldWidth);
  }

  @Override
  protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(heightMeasureSpec, widthMeasureSpec);
    setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
  }

  protected void onDraw(Canvas canvas) {
    canvas.rotate(-90);
    canvas.translate(-getHeight(), 0);

    super.onDraw(canvas);
  }

  public synchronized void setProgressAndThumb(int progress) {
    setProgress(getMax() - (getMax() - progress));
    onSizeChanged(getWidth(), getHeight(), 0, 0);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (!isEnabled()) {
      return false;
    }

    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
      case MotionEvent.ACTION_MOVE:
      case MotionEvent.ACTION_UP:
        int i = 0;
        i = getMax() - (int) (getMax() * event.getY() / getHeight());
        setProgress(i);
        onSizeChanged(getWidth(), getHeight(), 0, 0);
        break;

      case MotionEvent.ACTION_CANCEL:
        break;
    }
    return true;
  }
}