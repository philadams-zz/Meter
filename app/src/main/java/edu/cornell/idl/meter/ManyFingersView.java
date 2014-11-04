package edu.cornell.idl.meter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

/**
 * TODO make pretty!
 * TODO effectively bound the selector range
 */
public class ManyFingersView extends View {

  final static String TAG = "ManyFingersView";

  private SparseArray<PointF> activePointers;
  private Paint pointerPaint;
  private long lastScoreStagedTime = -1;
  private int pointerResetThreshold = 500;
  // milliseconds before resetting point count on new touch event

  private int pointerRadius = 100;

  public ManyFingersView(Context context) {
    this(context, null);
    init();
  }

  public ManyFingersView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public ManyFingersView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {
    activePointers = new SparseArray<PointF>();

    pointerPaint = new Paint();
    pointerPaint.setAntiAlias(true);
    pointerPaint.setColor(Color.RED);
    pointerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
  }

  /**
   * Keep track of how many fingers are touching this View.
   * The int reportedValue gets set, and the fingerpoints are retained
   * (a) if the number of fingers on the screen hasn't changed in 1 second
   */
  public boolean onTouchEvent(MotionEvent event) {
    int pointerIndex = event.getActionIndex();
    int pointerId = event.getPointerId(pointerIndex);

    switch (event.getActionMasked()) {  // masked action (not specific to a pointer)

      case MotionEvent.ACTION_DOWN:
      case MotionEvent.ACTION_POINTER_DOWN: {  // new pointer!

        // if we're over the pointerResetThreshold time and a new touch comes in,
        // reset the pointer count
        if (lastScoreStagedTime != -1
            && System.currentTimeMillis() - lastScoreStagedTime > pointerResetThreshold) {
          activePointers.clear();
          lastScoreStagedTime = -1;
        } else {
          lastScoreStagedTime = System.currentTimeMillis();
        }

        PointF f = new PointF();
        f.x = event.getX(pointerIndex);
        f.y = event.getY(pointerIndex);
        activePointers.put(pointerId, f);
        break;
      }
      case MotionEvent.ACTION_MOVE: { // a pointer was moved
        for (int size = event.getPointerCount(), i = 0; i < size; i++) {
          PointF point = activePointers.get(event.getPointerId(i));
          if (point != null) {
            point.x = event.getX(i);
            point.y = event.getY(i);
          }
        }
        break;
      }
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_POINTER_UP:
      case MotionEvent.ACTION_CANCEL: {
        //activePointers.remove(pointerId);
        break;
      }
    }
    invalidate();
    return true;
  }

  /**
   * onDraw
   */
  @Override
  protected void onDraw(Canvas canvas) {
    canvas.save(Canvas.MATRIX_SAVE_FLAG);
    //canvas.scale((float) getWidth(), (float) getHeight());

    // draw pointers under fingers
    int size = activePointers.size();
    for (int i = 0; i < size; i++) {
      PointF point = activePointers.valueAt(i);
      if (point != null) {
        canvas.drawCircle(point.x, point.y, pointerRadius, pointerPaint);
      }
    }
    //canvas.restore();
  }

  /**
   * SuperVAS should take up all available space.
   * TODO:philadams read xml attr for size
   */
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int widthDim = MeasureSpec.getSize(widthMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int heightDim = MeasureSpec.getSize(heightMeasureSpec);

    setMeasuredDimension(widthDim, heightDim);
  }

  public int getProgress() {
    return activePointers.size();
  }
}
