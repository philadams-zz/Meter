package edu.cornell.idl.meter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * TODO make pretty!
 * TODO effectively bound the selector range
 */
public class SuperVASView extends View {

  final static String TAG = "SuperVASView";

  private Paint sideScalePaint;

  private float sideScaleX = 0.05f;
  private float sideScaleY1 = 0.05f;
  private float sideScaleY2 = 1.0f - sideScaleY1;
  private float selectorTargetY = 0.5f;

  public SuperVASView(Context context) {
    this(context, null);
    init();
  }

  public SuperVASView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public SuperVASView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {
    sideScalePaint = new Paint();
    sideScalePaint.setAntiAlias(true);
    sideScalePaint.setStyle(Paint.Style.STROKE);
    sideScalePaint.setStrokeWidth(0.01f);
    sideScalePaint.setColor(Color.GRAY);
  }

  /**
   * onTouchEvent()
   */
  public boolean onTouchEvent(MotionEvent motionEvent) {
    float eventY = motionEvent.getY();
    float viewHeight = getHeight();
    switch (motionEvent.getAction()) {
      case MotionEvent.ACTION_DOWN:  // indicate user touching screen?
        return true;
      case MotionEvent.ACTION_MOVE:
        setSelectorTarget(eventY / viewHeight);
        return true;
      case MotionEvent.ACTION_UP:
        setSelectorTarget(eventY / viewHeight);  // update the selector to point at where user clicked
        return true;
      default:
        return super.onTouchEvent(motionEvent);
    }
  }

  private void setSelectorTarget(float target) {
    selectorTargetY = target;
    invalidate();
  }

  /**
   * onDraw
   */
  @Override
  protected void onDraw(Canvas canvas) {
    canvas.save(Canvas.MATRIX_SAVE_FLAG);
    canvas.scale((float) getWidth(), (float) getHeight());

    drawSideScale(canvas);
    drawSelector(canvas);

    canvas.restore();
  }

  private void drawSideScale(Canvas canvas) {
    Log.d(TAG, "drawSideScale");
    canvas.drawLine(sideScaleX, sideScaleY1, sideScaleX, sideScaleY2, sideScalePaint);
  }

  private void drawSelector(Canvas canvas) {
    Log.d(TAG, "drawSelector");
    canvas.drawLine(sideScaleX + 0.02f, selectorTargetY,
        1.0f - (sideScaleX + 0.02f), selectorTargetY,
        sideScalePaint);
  }

  /**
   * SuperVAS should take up all available space.
   * TODO:philadams read xml attr for size
   */
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    Log.d(TAG, String.format("width spec: %s", MeasureSpec.toString(widthMeasureSpec)));
    Log.d(TAG, String.format("height spec: %s", MeasureSpec.toString(heightMeasureSpec)));

    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int widthDim = MeasureSpec.getSize(widthMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int heightDim = MeasureSpec.getSize(heightMeasureSpec);

    setMeasuredDimension(widthDim, heightDim);
  }

  public int getProgress() {
    return 100 - (int) (selectorTargetY * 100);
  }

}
