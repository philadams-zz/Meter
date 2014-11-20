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
  private int ALPHA_MIN = 50;
  private int ALPHA_MAX = 255;
  private Paint reportedValuePaint;
  private String outOfTenText = "/10";
  private Paint outOfTenPaint;
  private PointF activePointer;
  private Paint tapUpPaint;
  private Paint tapDownPaint;

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

    // http://developer.android.com/guide/topics/graphics/hardware-accel.html
    setLayerType(LAYER_TYPE_SOFTWARE, null);

    reportedValue = initialValue;

    reportedValuePaint = new Paint();
    reportedValuePaint.setAntiAlias(true);
    reportedValuePaint.setColor(Color.parseColor("#000000"));
    reportedValuePaint.setTypeface(Typeface.SANS_SERIF);
    reportedValuePaint.setTypeface(Typeface.DEFAULT_BOLD);
    reportedValuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    reportedValuePaint.setTextAlign(Paint.Align.CENTER);
    reportedValuePaint.setTextSize(100f);
    //reportedValuePaint.setLinearText(true);

    outOfTenPaint = new Paint();
    outOfTenPaint.setAntiAlias(true);
    outOfTenPaint.setColor(Color.parseColor("#000000"));
    outOfTenPaint.setTypeface(Typeface.SANS_SERIF);
    outOfTenPaint.setTypeface(Typeface.DEFAULT_BOLD);
    outOfTenPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    outOfTenPaint.setTextAlign(Paint.Align.RIGHT);
    outOfTenPaint.setTextSize(24);
    //outOfTenPaint.setLinearText(true);

    tapUpPaint = new Paint();
    tapUpPaint.setColor(Color.parseColor("#EB6841"));
    tapUpPaint.setAntiAlias(true);
    tapUpPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    tapUpPaint.setStrokeCap(Paint.Cap.SQUARE);
    tapUpPaint.setAlpha(ALPHA_MIN);

    tapDownPaint = new Paint();
    tapDownPaint.setColor(Color.parseColor("#26ADE4"));
    tapDownPaint.setAntiAlias(true);
    tapDownPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    tapDownPaint.setStrokeCap(Paint.Cap.SQUARE);
    tapDownPaint.setAlpha(ALPHA_MIN);
  }

  /**
   * onSizeChanged we update the font sizes, so we're scaling relative to the screen size
   */
  @Override
  public void onSizeChanged(int w, int h, int oldw, int oldh) {
    reportedValuePaint.setTextSize(0.9f * w);
    outOfTenPaint.setTextSize(0.2f * w);
  }

  /**
   * onTouchEvent()
   */
  @Override
  public boolean onTouchEvent(MotionEvent motionEvent) {
    float eventX = motionEvent.getX();
    float eventY = motionEvent.getY();
    switch (motionEvent.getAction()) {
      case MotionEvent.ACTION_DOWN:
        activePointer = new PointF(eventX, eventY);
        highlightTapRegion(eventY / getHeight());
        invalidate();
        return true;
      case MotionEvent.ACTION_MOVE:
        return true;
      case MotionEvent.ACTION_UP:
        activePointer = null;
        handleTap(eventY / getHeight());
        lowlightTapRegion(eventY / getHeight());
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
    super.onDraw(canvas);

    drawTapRegions(canvas);
    drawOutOfTen(canvas);
    drawReportedValue(canvas);
  }

  /**
   * draw a rect to indicate a tappable region for incrementing reportedValue
   */
  protected void drawTapRegions(Canvas canvas) {
    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight() / 2 - 10, tapUpPaint);
    canvas.drawRect(0, canvas.getHeight() / 2 + 10, canvas.getWidth(), canvas.getHeight(), tapDownPaint);
  }

  /**
   * draw an 'out of 10' indicator below the reportedValue
   */
  protected void drawOutOfTen(Canvas canvas) {
    int xPos = canvas.getWidth();
    int yPos = (int) (canvas.getHeight() * 0.9f);
    canvas.drawText(outOfTenText, xPos, yPos, outOfTenPaint);
  }

  /**
   * drawReportedValue(Canvas canvas)
   */
  protected void drawReportedValue(Canvas canvas) {
    int xPos = canvas.getWidth() / 2;
    int yPos = (int) ((canvas.getHeight() / 2) - ((reportedValuePaint.descent() + reportedValuePaint.ascent()) /2));
    canvas.drawText(String.valueOf(reportedValue), xPos, yPos, reportedValuePaint);
  }

  /**
   * If the tapPosition is less than .5, decrement reportedValue. Else, increment it.
   */
  protected void handleTap(float tapPosition) {
    int delta = (tapPosition < 0.5) ? 1 : -1;
    reportedValue = Utility.clamp(reportedValue + delta, 0, 10);
  }

  /**
   * If there's an activePointer, make that tappable region completely opaque
   */
  protected void highlightTapRegion(float tapPosition) {
    if (activePointer != null) {
      if (tapPosition < 0.5) {
        tapUpPaint.setAlpha(ALPHA_MAX);
      } else {
        tapDownPaint.setAlpha(ALPHA_MAX);
      }
    }
  }

  /**
   * If there's an activePointer, make that tappable region completely opaque
   */
  protected void lowlightTapRegion(float tapPosition) {
    tapUpPaint.setAlpha(ALPHA_MIN);
    tapDownPaint.setAlpha(ALPHA_MIN);
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
