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
public class SuperVASNumberedView extends View {

  final static String TAG = "SuperVASView";

  // selector knob/circle
  private Paint selectorInnerPaint;
  private Paint selectorOuterPaint;
  private float radius = 50;

  // scale line/bar thingy
  private Paint scaleSelectedPaint;
  private Paint scaleUnselectedPaint;
  private float scaleX = 200;
  private float scaleY1 = 0.0f;
  private float scaleY2 = 400 - scaleY1;
  private float selectorTargetY = 100f;
  private float padding = radius;  // offset from the edges of the scale

  private float minTargetY = scaleY1 + padding + radius/2;
  private float maxTargetY = scaleY2 + radius/2;

  public SuperVASNumberedView(Context context) {
    this(context, null);
    init();
  }

  public SuperVASNumberedView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public SuperVASNumberedView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {
    scaleUnselectedPaint = new Paint();
    scaleUnselectedPaint.setAntiAlias(true);
    scaleUnselectedPaint.setStyle(Paint.Style.STROKE);
    scaleUnselectedPaint.setStrokeWidth(3);
    scaleUnselectedPaint.setColor(Color.LTGRAY);

    scaleSelectedPaint = new Paint();
    scaleSelectedPaint.setAntiAlias(true);
    scaleSelectedPaint.setStyle(Paint.Style.STROKE);
    scaleSelectedPaint.setStrokeWidth(12);
    scaleSelectedPaint.setColor(getResources().getColor(android.R.color.holo_blue_light));

    selectorInnerPaint = new Paint();
    selectorInnerPaint.setAntiAlias(true);
    selectorInnerPaint.setStyle(Paint.Style.FILL);
    selectorInnerPaint.setColor(getResources().getColor(android.R.color.holo_blue_light));

    selectorOuterPaint = new Paint();
    selectorOuterPaint.setAntiAlias(true);
    selectorOuterPaint.setStyle(Paint.Style.FILL);
    selectorOuterPaint.setStrokeWidth(4);
    selectorOuterPaint.setColor(getResources().getColor(android.R.color.holo_blue_light));
    selectorOuterPaint.setAlpha(125);  // must come after setColor()
  }

  /**
   * onDraw
   */
  @Override
  protected void onDraw(Canvas canvas) {
    drawScaleBar(canvas);
    drawSelectorKnob(canvas);
  }

  /**
   * draw scale (vertical line)
   * @param canvas
   */
  private void drawScaleBar(Canvas canvas) {
    canvas.drawLine(scaleX, scaleY1, scaleX, selectorTargetY, scaleUnselectedPaint);
    canvas.drawLine(scaleX, selectorTargetY, scaleX, scaleY2, scaleSelectedPaint);
  }

  /**
   * draw selector (the little knob/circle thing)
   * @param canvas
   */
  private void drawSelectorKnob(Canvas canvas) {
    canvas.drawCircle(scaleX, selectorTargetY, radius, selectorOuterPaint);
    canvas.drawCircle(scaleX, selectorTargetY, radius - 35, selectorInnerPaint);
  }


  /**
   * onTouchEvent()
   */
  public boolean onTouchEvent(MotionEvent motionEvent) {
    switch (motionEvent.getAction()) {
      case MotionEvent.ACTION_DOWN:  // indicate user touching screen?
        return true;
      case MotionEvent.ACTION_MOVE:
        setSelectorTarget(motionEvent.getY());
        return true;
      case MotionEvent.ACTION_UP:
        setSelectorTarget(motionEvent.getY());  // update the selector to point at where user clicked
        return true;
      default:
        return super.onTouchEvent(motionEvent);
    }
  }

  ///**
  // * drawReportedValue(Canvas canvas)
  // */
  //protected void drawReportedValue(Canvas canvas) {
  //  int xPos = canvas.getWidth() / 2;
  //  int yPos = (int) ((canvas.getHeight() / 2) - getVerticalCenterOffsetForPaint(reportedValuePaint));
  //  canvas.drawText(String.valueOf(reportedValue), xPos, yPos, reportedValuePaint);
  //}

  private void setSelectorTarget(float target) {
    selectorTargetY = target;
    if (selectorTargetY < minTargetY) {
      selectorTargetY = minTargetY;
    } else if (selectorTargetY > maxTargetY) selectorTargetY = maxTargetY;
    invalidate();
  }

  /**
   * For a given (text) paint, compute it's vertical centering offset
   */
  protected float getVerticalCenterOffsetForPaint(Paint mPaint) {
    return (mPaint.descent() + mPaint.ascent()) / 2;
  }

  /**
   * onSizeChanged we update the font sizes, so we're scaling relative to the screen size
   */
  @Override
  public void onSizeChanged(int w, int h, int oldw, int oldh) {
    scaleX = 0.2f * w;
    scaleY1 = 0 + padding;
    scaleY2 = h - padding;
    selectorTargetY = h / 2;
    minTargetY = scaleY1;
    maxTargetY = scaleY2;
    //reportedValuePaint.setTextSize(0.9f * w);
    //outOfTenPaint.setTextSize(0.2f * w);
    //plusMinusPaint.setTextSize(0.2f * w);
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
    return 10 - (int) Utility.linearlyScale(selectorTargetY, minTargetY, maxTargetY, 0.0f, 10.0f);
  }
}
