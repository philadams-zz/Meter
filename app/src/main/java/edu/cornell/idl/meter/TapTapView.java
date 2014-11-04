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
 * View that listens for single taps, and increments pain value accordingly.
 * The tap following reportedValue = maxValue loops back to 0.
 * Perhaps show the 0..5 along the bottom or top, with the anchors, as well as the big number in
 * the
 * middle?
 */
public class TapTapView extends View {

  final static String TAG = "TapTapView";
  private int reportedValue;
  private int maxValue = 10;
  private Paint reportedValuePaint;

  public TapTapView(Context context) {
    this(context, null);
    init();
  }

  public TapTapView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public TapTapView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {

    setLayerType(LAYER_TYPE_SOFTWARE,
        null);  // http://developer.android.com/guide/topics/graphics/hardware-accel.html

    reportedValue = 0;
    reportedValuePaint = new Paint();
    reportedValuePaint.setAntiAlias(true);
    reportedValuePaint.setColor(Color.BLUE);
    reportedValuePaint.setTypeface(Typeface.SANS_SERIF);
    reportedValuePaint.setTypeface(Typeface.DEFAULT_BOLD);
    reportedValuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    reportedValuePaint.setTextAlign(Paint.Align.CENTER);
    reportedValuePaint.setTextSize(0.9f);
    reportedValuePaint.setLinearText(true);
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
        return true;
      case MotionEvent.ACTION_UP:
        reportedValue = (reportedValue == maxValue) ? 0 : reportedValue + 1;
        invalidate();
        return true;
      default:
        return super.onTouchEvent(motionEvent);
    }
  }

  /**
   * onDraw
   */
  @Override
  protected void onDraw(Canvas canvas) {
    canvas.save(Canvas.MATRIX_SAVE_FLAG);
    canvas.scale((float) getWidth(), (float) getHeight());

    drawReportedValue(canvas);

    canvas.restore();
  }

  /**
   * drawReportedValue(int value)
   */
  protected void drawReportedValue(Canvas canvas) {
    canvas.drawText(String.valueOf(reportedValue), 0.5f, 0.8f, reportedValuePaint);
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
    return reportedValue;
  }
}
