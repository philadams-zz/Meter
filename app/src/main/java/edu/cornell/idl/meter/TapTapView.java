package edu.cornell.idl.meter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * View that listens for taps: on the top half of the screen increment the value,
 * on the bottom decrement.
 */
public class TapTapView extends View {

  final static String TAG = "TapTapView";
  private int reportedValue;
  private int initialValue = 5;
  private Paint reportedValuePaint;
  private String outOfTenText = "/10";
  private Paint outOfTenPaint;
  private PointF activePointer;
  private Paint activePointerPaint;

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

    reportedValue = initialValue;
    
    reportedValuePaint = new Paint();
    reportedValuePaint.setAntiAlias(true);
    reportedValuePaint.setColor(Color.BLUE);
    reportedValuePaint.setTypeface(Typeface.SANS_SERIF);
    reportedValuePaint.setTypeface(Typeface.DEFAULT_BOLD);
    reportedValuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    reportedValuePaint.setTextAlign(Paint.Align.CENTER);
    reportedValuePaint.setTextSize(0.9f);
    reportedValuePaint.setLinearText(true);

    outOfTenPaint = new Paint();
    outOfTenPaint.setAntiAlias(true);
    outOfTenPaint.setColor(Color.BLUE);
    outOfTenPaint.setTypeface(Typeface.SANS_SERIF);
    outOfTenPaint.setTypeface(Typeface.DEFAULT_BOLD);
    outOfTenPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    outOfTenPaint.setTextAlign(Paint.Align.RIGHT);
    outOfTenPaint.setTextSize(0.1f);
    outOfTenPaint.setLinearText(true);

    activePointerPaint = new Paint();
    activePointerPaint.setAntiAlias(true);
    activePointerPaint.setColor(Color.RED);
    activePointerPaint.setTypeface(Typeface.SANS_SERIF);
    activePointerPaint.setTypeface(Typeface.DEFAULT_BOLD);
    activePointerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    activePointerPaint.setTextAlign(Paint.Align.RIGHT);
    activePointerPaint.setTextSize(0.4f);
    activePointerPaint.setLinearText(true);
  }

  /**
   * onTouchEvent()
   */
  public boolean onTouchEvent(MotionEvent motionEvent) {
    float eventX = motionEvent.getX();
    float viewWidth = getWidth();
    float eventY = motionEvent.getY();
    float viewHeight = getHeight();
    switch (motionEvent.getAction()) {
      case MotionEvent.ACTION_DOWN:
        activePointer = new PointF(eventX / viewWidth , eventY / viewHeight);
        invalidate();
        return true;
      case MotionEvent.ACTION_MOVE:
        return true;
      case MotionEvent.ACTION_UP:
        activePointer = null;
        handleTap(eventY / viewHeight);
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
    drawOutOfTen(canvas);
    drawActivePointer(canvas);

    canvas.restore();
  }

  /**
   * draw an 'out of 10' indicator below the reportedValue
   */
  protected void drawOutOfTen(Canvas canvas) {
    canvas.drawText(outOfTenText, 1, 0.9f, outOfTenPaint);
  }

  /**
   * drawReportedValue(Canvas canvas)
   */
  protected void drawReportedValue(Canvas canvas) {
    canvas.drawText(String.valueOf(reportedValue), 0.5f, 0.8f, reportedValuePaint);
  }

  /**
   * show a plus or minus icon when the user taps the screen, depending on vertical position
   */
  protected void drawActivePointer(Canvas canvas) {
    if (activePointer != null) {
      String activePointerSymbol = (activePointer.y < 0.5) ? "+" : "-";
      canvas.drawText(activePointerSymbol, activePointer.x, activePointer.y, activePointerPaint);
    }
  }

  /**
   * If the tapPosition is less than .5, decrement reportedValue. Else, increment it.
   */
  protected void handleTap(float tapPosition) {
    int delta = (tapPosition < 0.5) ? 1 : -1;
    reportedValue = Utility.clamp(reportedValue + delta, 0, 10);
  }

  /**
   * This view should take up all available space.
   */
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int widthDim = MeasureSpec.getSize(widthMeasureSpec);
    int heightDim = MeasureSpec.getSize(heightMeasureSpec);
    setMeasuredDimension(widthDim, heightDim);
  }

  public int getProgress() {
    return reportedValue;
  }
}
