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

  private Paint scalePaint;

  private Paint selectorPaint;

  private float minTargetY = 0.05f;
  private float maxTargetY = 1.0f - minTargetY;

  private float scaleX = 0.5f;
  private float scaleY1 = minTargetY;
  private float scaleY2 = 1.0f - scaleY1;
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
    scalePaint = new Paint();
    scalePaint.setAntiAlias(true);
    scalePaint.setStyle(Paint.Style.STROKE);
    scalePaint.setStrokeWidth(0.005f);
    scalePaint.setColor(Color.GRAY);

    selectorPaint = new Paint();
    selectorPaint.setAntiAlias(true);
    selectorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    selectorPaint.setStrokeWidth(0.005f);
    selectorPaint.setColor(Color.GRAY);
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
    if (selectorTargetY < minTargetY)
      selectorTargetY = minTargetY;
    else if (selectorTargetY > maxTargetY)
      selectorTargetY = maxTargetY;
    invalidate();
  }

  /**
   * onDraw
   */
  @Override
  protected void onDraw(Canvas canvas) {
    canvas.save(Canvas.MATRIX_SAVE_FLAG);
    canvas.scale((float) getWidth(), (float) getHeight());

    drawScale(canvas);
    drawSelector(canvas);

    canvas.restore();
  }

  private void drawScale(Canvas canvas) {
    canvas.drawLine(scaleX, scaleY1, scaleX, scaleY2, scalePaint);
  }

  private void drawSelector(Canvas canvas) {
    canvas.drawCircle(0.5f, selectorTargetY, 0.025f, selectorPaint);
  }

  /**
   * SuperVAS should take up all available space.
   * TODO:philadams read xml attr for size
   */
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    //Log.d(TAG, String.format("width spec: %s", MeasureSpec.toString(widthMeasureSpec)));
    //Log.d(TAG, String.format("height spec: %s", MeasureSpec.toString(heightMeasureSpec)));

    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int widthDim = MeasureSpec.getSize(widthMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int heightDim = MeasureSpec.getSize(heightMeasureSpec);

    setMeasuredDimension(widthDim, heightDim);
  }

  public int getProgress() {
    return 100 - (int) Utility.linearlyScale(selectorTargetY, minTargetY, maxTargetY, 0.0f, 100.0f);
  }

}
