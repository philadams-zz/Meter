package edu.cornell.idl.meter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

/**
 */
public class SAFESliderView extends View {

  final static String TAG = "SAFESliderView";

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

  // SAFE faces
  final String FACES_DIR = "safe_faces_transparentbg";
  final int TOTAL_NUM_FACES = 101;
  final int NUM_FACES = 16;
  Bitmap[] faces;
  int face_to_show;
  Rect faceRect = new Rect(0, 0, 200, 200);
  Paint facePaint;

  public SAFESliderView(Context context) {
    this(context, null);
    init();
  }

  public SAFESliderView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public SAFESliderView(Context context, AttributeSet attrs, int defStyle) {
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

    // preload bitmaps for each of the faces in FACES_DIR
    facePaint = new Paint();
    facePaint.setDither(false);
    preloadFaceBitmaps();
    face_to_show = Utility.clamp(19, 0, NUM_FACES - 1);
  }

  /**
   * onDraw
   */
  @Override
  protected void onDraw(Canvas canvas) {
    drawFace(canvas);
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
   * draw one of the SAFE faces, based on where the slider's currently pointing
   * @param canvas
   */
  private void drawFace(Canvas canvas) {
    canvas.drawBitmap(faces[face_to_show], null, faceRect, facePaint);
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

  private void setSelectorTarget(float target) {

    // update the selector target position
    selectorTargetY = target;
    if (selectorTargetY < minTargetY) {
      selectorTargetY = minTargetY;
    } else if (selectorTargetY > maxTargetY) selectorTargetY = maxTargetY;

    // and update face_to_show
    face_to_show = getSAFEFaceIndexByScreenPosition(selectorTargetY / scaleY2);

    invalidate();
  }

  private void preloadFaceBitmaps() {

    faces = new Bitmap[NUM_FACES];
    InputStream inputStream;
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    AssetManager assetManager = getContext().getAssets();
    DecimalFormat df = new DecimalFormat("0");
    df.setMinimumIntegerDigits(2);

    try {
      int faceFile = 0;
      int step = Utility.clamp((int) ((float) TOTAL_NUM_FACES / (float) NUM_FACES), 1, NUM_FACES);
      for (int i = 0; i < NUM_FACES; i++) {
        inputStream = assetManager.open(FACES_DIR + "/export" + df.format(faceFile) + "t.bmp.png");
        Bitmap face = BitmapFactory.decodeStream(inputStream);
        face.setHasAlpha(true);
        faces[i] = face;
        faceFile = Utility.clamp(faceFile += step, 0, TOTAL_NUM_FACES - 1);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * given position on screen, return the appropriate SAFE face index
   * @param position
   * @return
   */
  private int getSAFEFaceIndexByScreenPosition(float position) {
    return NUM_FACES - 1 - Utility.clamp(Math.round(position * NUM_FACES - 1), 0, NUM_FACES - 1);
  }

  /**
   * onSizeChanged we update the font sizes, so we're scaling relative to the screen size
   */
  @Override
  public void onSizeChanged(int w, int h, int oldw, int oldh) {
    scaleX = 0.1f * w;
    scaleY1 = 0 + padding;
    scaleY2 = h - padding;
    selectorTargetY = 0.8f * h;
    minTargetY = scaleY1;
    maxTargetY = scaleY2;
    faceRect = new Rect((int) (0.2f * w), (int) scaleY1, w, (int) scaleY2);
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
