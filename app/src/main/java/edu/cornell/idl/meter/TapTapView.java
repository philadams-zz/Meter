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

    // http://developer.android.com/guide/topics/graphics/hardware-accel.html
    setLayerType(LAYER_TYPE_SOFTWARE, null);

    reportedValue = initialValue;

    reportedValuePaint = new Paint();
    reportedValuePaint.setAntiAlias(true);
    reportedValuePaint.setColor(Color.BLUE);
    reportedValuePaint.setTypeface(Typeface.SANS_SERIF);
    reportedValuePaint.setTypeface(Typeface.DEFAULT_BOLD);
    reportedValuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    reportedValuePaint.setTextAlign(Paint.Align.CENTER);
    reportedValuePaint.setTextSize(100f);
    reportedValuePaint.setLinearText(true);

    outOfTenPaint = new Paint();
    outOfTenPaint.setAntiAlias(true);
    outOfTenPaint.setColor(Color.BLUE);
    outOfTenPaint.setTypeface(Typeface.SANS_SERIF);
    outOfTenPaint.setTypeface(Typeface.DEFAULT_BOLD);
    outOfTenPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    outOfTenPaint.setTextAlign(Paint.Align.RIGHT);
    outOfTenPaint.setTextSize(24);
    outOfTenPaint.setLinearText(true);

    activePointerPaint = new Paint();
    activePointerPaint.setAntiAlias(true);
    activePointerPaint.setColor(Color.RED);
    activePointerPaint.setTypeface(Typeface.SANS_SERIF);
    activePointerPaint.setTypeface(Typeface.DEFAULT_BOLD);
    activePointerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    activePointerPaint.setTextAlign(Paint.Align.RIGHT);
    activePointerPaint.setTextSize(40);
    activePointerPaint.setLinearText(true);
  }

  /**
   * onSizeChanged we update the font sizes, so we're scaling relative to the screen size
   */
  @Override
  public void onSizeChanged(int w, int h, int oldw, int oldh) {
    reportedValuePaint.setTextSize(0.9f * w);
    outOfTenPaint.setTextSize(0.2f * w);
    activePointerPaint.setTextSize(0.4f * w);
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
        invalidate();
        return true;
      case MotionEvent.ACTION_MOVE:
        return true;
      case MotionEvent.ACTION_UP:
        activePointer = null;
        handleTap(eventY / getHeight());
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

    //canvas.save(Canvas.MATRIX_SAVE_FLAG);
    //canvas.scale(getWidth(), getHeight());

    drawOutOfTen(canvas);
    drawReportedValue(canvas);
    drawActivePointer(canvas);

    //canvas.restore();
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
   * show a plus or minus icon when the user taps the screen, depending on vertical position
   */
  protected void drawActivePointer(Canvas canvas) {
    if (activePointer != null) {
      String activePointerSymbol = (activePointer.y / canvas.getHeight() < 0.5) ? "+" : "-";
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
