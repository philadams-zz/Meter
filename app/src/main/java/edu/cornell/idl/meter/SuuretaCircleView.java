package edu.cornell.idl.meter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * A circle for the Suureta meter that grows when the screen is touched.
 */
public class SuuretaCircleView extends View {
  private final int paintColor = Color.BLACK;
  private final int refreshRate = 5;  // smaller values grow suureta more quickly
  private Paint paint;
  private int radius;
  private Handler mHandler;
  private Runnable mGrow;

  public SuuretaCircleView(Context context, AttributeSet attrs) {
    super(context, attrs);
    setFocusable(true);
    setFocusableInTouchMode(true);
    init();
  }

  private void init() {
    paint = new Paint();
    paint.setColor(paintColor);
    paint.setAntiAlias(true);
    paint.setStrokeWidth(5);
    paint.setStyle(Paint.Style.FILL_AND_STROKE);
    paint.setStrokeJoin(Paint.Join.ROUND);
    paint.setStrokeCap(Paint.Cap.ROUND);
    paint.setTextSize(100);
    paint.setTextAlign(Paint.Align.CENTER);
    radius = 20;
    mGrow = new Runnable() {
      @Override
      public void run() {
        radius += 2;
        mHandler.postDelayed(this, 10);
        postInvalidate();
      }
    };
  }

  @Override
  protected void onDraw(Canvas canvas) {
    int centerX = canvas.getWidth() / 2;
    int centerY = canvas.getHeight() / 2;
    paint.setColor(Color.BLACK);
    canvas.drawCircle(centerX, centerY, radius, paint);
    paint.setColor(Color.RED);
    canvas.drawText(Integer.toString(radius), centerX, centerY, paint);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    float pointX = event.getX();
    float pointY = event.getY();

    switch(event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        mHandler = new Handler();
        mHandler.postDelayed(mGrow, refreshRate);
        break;
      case MotionEvent.ACTION_UP:
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
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int width = getMeasuredWidth();
    int height = getMeasuredHeight();
    setMeasuredDimension(100, 100);
  }

  public int getRadius() {
    return this.radius;
  }
}