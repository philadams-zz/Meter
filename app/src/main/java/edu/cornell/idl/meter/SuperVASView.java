package edu.cornell.idl.meter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 */
public class SuperVASView extends View {

  final static String TAG = "SuperVASView";

  private Paint scaleSelectedPaint;
  private Paint scaleUnselectedPaint;

  private Paint selectorInnerPaint;
  private Paint selectorOuterPaint;

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
    scaleUnselectedPaint = new Paint();
    scaleUnselectedPaint.setAntiAlias(true);
    scaleUnselectedPaint.setStyle(Paint.Style.STROKE);
    scaleUnselectedPaint.setStrokeWidth(0.005f);
    scaleUnselectedPaint.setColor(Color.LTGRAY);

    scaleSelectedPaint = new Paint();
    scaleSelectedPaint.setAntiAlias(true);
    scaleSelectedPaint.setStyle(Paint.Style.STROKE);
    scaleSelectedPaint.setStrokeWidth(0.01f);
    scaleSelectedPaint.setColor(getResources().getColor(android.R.color.holo_blue_light));

    selectorInnerPaint = new Paint();
    selectorInnerPaint.setAntiAlias(true);
    selectorInnerPaint.setStyle(Paint.Style.FILL);
    selectorInnerPaint.setColor(getResources().getColor(android.R.color.holo_blue_light));

    selectorOuterPaint = new Paint();
    selectorOuterPaint.setAntiAlias(true);
    selectorOuterPaint.setStyle(Paint.Style.FILL);
    selectorOuterPaint.setStrokeWidth(0.01f);
    selectorOuterPaint.setColor(getResources().getColor(android.R.color.holo_blue_light));
    selectorOuterPaint.setAlpha(125);  // must come after setColor()
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
        setSelectorTarget(
            eventY / viewHeight);  // update the selector to point at where user clicked
        return true;
      default:
        return super.onTouchEvent(motionEvent);
    }
  }

  private void setSelectorTarget(float target) {
    selectorTargetY = target;
    if (selectorTargetY < minTargetY) {
      selectorTargetY = minTargetY;
    } else if (selectorTargetY > maxTargetY) selectorTargetY = maxTargetY;
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
    canvas.drawLine(scaleX, scaleY1, scaleX, selectorTargetY, scaleUnselectedPaint);
    canvas.drawLine(scaleX, selectorTargetY, scaleX, scaleY2, scaleSelectedPaint);
  }

  private void drawSelector(Canvas canvas) {
    canvas.drawCircle(0.5f, selectorTargetY, 0.04f, selectorOuterPaint);
    canvas.drawCircle(0.5f, selectorTargetY, 0.015f, selectorInnerPaint);
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
    return 100 - (int) Utility.linearlyScale(selectorTargetY, minTargetY, maxTargetY, 0.0f, 100.0f);
  }
}
