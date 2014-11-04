package edu.cornell.idl.meter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 *
 */
public class SuperVASPlusView extends View {

  final static String TAG = SuperVASPlusView.class.getSimpleName();

  private Paint scaleSelectedPaint;  // the lower part of the bar that is 'included'
  private Paint numericIndicatorPaint;  // the red number

  private float minTargetY = 0.05f;
  private float maxTargetY = 1.0f - minTargetY;

  private float scaleX = 0.5f;
  private float scaleY1 = minTargetY;
  private float scaleY2 = 1.0f - scaleY1;
  private float selectorTargetY = 0.5f;

  public SuperVASPlusView(Context context) {
    this(context, null);
    init();
  }

  public SuperVASPlusView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public SuperVASPlusView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {
    scaleSelectedPaint = new Paint();
    scaleSelectedPaint.setAntiAlias(true);
    scaleSelectedPaint.setStyle(Paint.Style.FILL);
    scaleSelectedPaint.setColor(getResources().getColor(android.R.color.holo_blue_light));

    numericIndicatorPaint = new Paint();
    numericIndicatorPaint.setColor(getResources().getColor(android.R.color.holo_red_dark));
    numericIndicatorPaint.setAntiAlias(true);
    numericIndicatorPaint.setStrokeWidth(0.01f);
    numericIndicatorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    numericIndicatorPaint.setStrokeJoin(Paint.Join.ROUND);
    numericIndicatorPaint.setStrokeCap(Paint.Cap.ROUND);
    numericIndicatorPaint.setTextSize(0.2f);
    numericIndicatorPaint.setTextAlign(Paint.Align.CENTER);

    // weird fixes for (i think) https://code.google.com/p/android/issues/detail?id=40965
    numericIndicatorPaint.setLinearText(true);
    setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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
        setSelectorTarget(eventY / viewHeight);  // update the selector to where user clicked
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
    canvas.drawRect(0f, selectorTargetY, 1f, 1f, scaleSelectedPaint);
  }

  private void drawSelector(Canvas canvas) {
    canvas.drawText(Integer.toString(getProgress()), 0.5f, 0.5f, numericIndicatorPaint);
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
