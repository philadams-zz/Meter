package edu.cornell.idl.meter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * A circle for the Suureta meter that grows when the screen is touched.
 */
public class SuuretaView extends View {

  final static String TAG = "SuuretaView";

  private final int RADIUS_DELTA = 10;
  private int radiusDelta;  // larger values grow suureta more quickly
  private Paint circlePaint;
  private Paint reportedValuePaint;
  private Paint activePointerPaint;
  private PointF activePointer;
  private int radius;
  private int minRadius;
  private int maxRadius;
  private Handler mHandler;
  private Runnable mGrow;

  public SuuretaView(Context context, AttributeSet attrs) {
    super(context, attrs);
    setFocusable(true);
    setFocusableInTouchMode(true);
    init();
  }

  private void init() {
    minRadius = 0;
    maxRadius = 1000;
    radius = (maxRadius - minRadius) / 2;
    radiusDelta = RADIUS_DELTA;

    circlePaint = new Paint();
    circlePaint.setColor(Color.BLUE);
    circlePaint.setAntiAlias(true);
    circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    circlePaint.setStrokeJoin(Paint.Join.ROUND);
    circlePaint.setStrokeCap(Paint.Cap.ROUND);

    reportedValuePaint = new Paint();
    reportedValuePaint.setAntiAlias(true);
    reportedValuePaint.setColor(Color.RED);
    reportedValuePaint.setTypeface(Typeface.SANS_SERIF);
    reportedValuePaint.setTypeface(Typeface.DEFAULT_BOLD);
    reportedValuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    reportedValuePaint.setTextAlign(Paint.Align.CENTER);
    reportedValuePaint.setTextSize(0.4f);
    reportedValuePaint.setLinearText(true);

    activePointerPaint = new Paint();
    activePointerPaint.setAntiAlias(true);
    activePointerPaint.setColor(Color.GREEN);
    activePointerPaint.setTypeface(Typeface.SANS_SERIF);
    activePointerPaint.setTypeface(Typeface.DEFAULT_BOLD);
    activePointerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    activePointerPaint.setTextAlign(Paint.Align.CENTER);
    activePointerPaint.setTextSize(0.4f);
    activePointerPaint.setLinearText(true);

    mGrow = new Runnable() {
      @Override
      public void run() {
        radius = Utility.clamp(radius + radiusDelta, minRadius, maxRadius);
        mHandler.postDelayed(this, 10);
        postInvalidate();
      }
    };

    setLayerType(LAYER_TYPE_SOFTWARE, null);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    canvas.save(Canvas.MATRIX_SAVE_FLAG);
    canvas.scale((float) getWidth(), (float) getHeight());

    drawSuuretaCircle(canvas);
    drawReportedValue(canvas);
    drawActivePointer(canvas);

    canvas.restore();
  }

  protected void drawSuuretaCircle(Canvas canvas) {
    //canvas.drawCircle(0.5f, 0.5f, Utility.linearlyScale(radius, minRadius, maxRadius, 0f, 0.45f),
    //    circlePaint);  // this is stretched at the ratio of getWidth/getHeight
    float scale = (float) getWidth() / (float) getHeight();
    float scaledRadius = Utility.linearlyScale(radius, minRadius, maxRadius, 0f, 0.48f);
    RectF bounds = new RectF(0.5f - scaledRadius, 0.5f - scaledRadius * scale, 0.5f + scaledRadius,
        0.5f + scaledRadius * scale);
    canvas.drawOval(bounds, circlePaint);
  }

  protected void drawReportedValue(Canvas canvas) {
    canvas.drawText(Integer.toString(Utility.linearlyScale(radius, minRadius, maxRadius, 0, 10)),
        0.5f, 0.5f - (reportedValuePaint.descent() + reportedValuePaint.ascent()) / 2,
        reportedValuePaint);
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

  @Override
  public boolean onTouchEvent(MotionEvent motionEvent) {
    float touchX = motionEvent.getX();
    float touchY = motionEvent.getY();

    radiusDelta = (touchY / getHeight() < 0.5) ? RADIUS_DELTA : -1 * RADIUS_DELTA;

    switch (motionEvent.getAction()) {
      case MotionEvent.ACTION_DOWN:
        activePointer = new PointF(touchX / getWidth(), touchY / getHeight());
        mHandler = new Handler();
        mHandler.postDelayed(mGrow, radiusDelta);
        break;
      case MotionEvent.ACTION_MOVE:
        if (activePointer != null) {
          activePointer.set(touchX / getWidth(), touchY / getHeight());
        }
        break;
      case MotionEvent.ACTION_UP:
        activePointer = null;
        if (mHandler == null) return true;
        mHandler.removeCallbacks(mGrow);
        mHandler = null;
        break;
      default:
        return false;
    }

    postInvalidate();  // indicate view should be redrawn
    return true;  // indicate touch has been consumed
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int widthDim = MeasureSpec.getSize(widthMeasureSpec);
    int heightDim = MeasureSpec.getSize(heightMeasureSpec);
    setMeasuredDimension(widthDim, heightDim);
  }

  public int getProgress() {
    return Utility.linearlyScale(radius, 0, maxRadius, 0, 10);
  }
}