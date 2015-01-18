package edu.cornell.idl.meter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 */
public class SuperVASNumberedView extends View {

  final static String TAG = "SuperVASNumberedView";

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

  private float minTargetY = scaleY1;
  private float maxTargetY = scaleY2;

  // reported value numeral
  private Paint reportedValuePaint;
  private float reportedValueX = 200f;
  private float reportedValueY = 200f;

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

    reportedValuePaint = new Paint();
    reportedValuePaint.setAntiAlias(true);
    reportedValuePaint.setColor(Color.parseColor("#222222"));
    reportedValuePaint.setTypeface(Typeface.SANS_SERIF);
    reportedValuePaint.setTypeface(Typeface.DEFAULT_BOLD);
    reportedValuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    reportedValuePaint.setTextAlign(Paint.Align.CENTER);
    reportedValuePaint.setTextSize(76f);
  }

  /**
   * onDraw
   */
  @Override
  protected void onDraw(Canvas canvas) {
    drawScaleBar(canvas);
    drawSelectorKnob(canvas);
    drawReportedValue(canvas);
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
   * when the user touches the screen, update the selector to point where the user taps
   */
  public boolean onTouchEvent(MotionEvent motionEvent) {
    switch (motionEvent.getAction()) {
      case MotionEvent.ACTION_DOWN:  // TODO:philadams indicate user touching screen??? haptics?
        return true;
      case MotionEvent.ACTION_MOVE:
        setSelectorTarget(motionEvent.getY());
        return true;
      case MotionEvent.ACTION_UP:
        setSelectorTarget(motionEvent.getY());
        return true;
      default:
        return super.onTouchEvent(motionEvent);
    }
  }

  /**
  * drawReportedValue(Canvas canvas)
  */
  protected void drawReportedValue(Canvas canvas) {
    canvas.drawText(String.valueOf(getProgress()), reportedValueX, reportedValueY, reportedValuePaint);
  }

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
    scaleX = 0.15f * w;
    scaleY1 = 0 + padding;
    scaleY2 = h - padding;
    selectorTargetY = 0.8f * h;
    minTargetY = scaleY1;
    maxTargetY = scaleY2;
    reportedValuePaint.setTextSize(0.7f * w);
    reportedValueX = 0.55f * w;
    reportedValueY = ((h / 2) - getVerticalCenterOffsetForPaint(reportedValuePaint));
  }

  /**
   * SuperVAS should take up all available space.
   * TODO:philadams read xml attr for size
   */
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int widthDim = MeasureSpec.getSize(widthMeasureSpec);
    int heightDim = MeasureSpec.getSize(heightMeasureSpec);
    setMeasuredDimension(widthDim, heightDim);
  }

  public int getProgress() {
    return 10 - (int) Utility.linearlyScale(selectorTargetY, minTargetY, maxTargetY, 0.0f, 10.0f);
  }
}
