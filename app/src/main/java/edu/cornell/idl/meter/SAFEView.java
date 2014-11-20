package edu.cornell.idl.meter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

/**
 * TODO: SAFE crashes on nexus 6 (out of memory)
 */
public class SAFEView extends View {

  final static String TAG = "SAFEView";
  final String FACES_DIR = "safe_faces";
  final int NUM_FACES = 101;
  Bitmap[] faces = new Bitmap[NUM_FACES];
  final int FIRST_FACE_TO_SHOW = 19;
  int face_to_show = FIRST_FACE_TO_SHOW;

  public SAFEView(Context context) {
    this(context, null);
    init();
  }

  public SAFEView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public SAFEView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {

    // preload bitmaps for each of the faces in /assets/safe_faces
    InputStream inputStream;
    AssetManager assetManager = getContext().getAssets();
    DecimalFormat df = new DecimalFormat("0");
    df.setMinimumIntegerDigits(2);
    try {
      //String[] fnames = assetManager.list(FACES_DIR);  // this is super slow! and not in alpha order...
      for (int i = 0; i < NUM_FACES; i++) {
        inputStream = assetManager.open(FACES_DIR + "/export" + df.format(i) + ".bmp");
        Bitmap face = BitmapFactory.decodeStream(inputStream);
        faces[i] = face;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * onDraw
   */
  @Override
  protected void onDraw(Canvas canvas) {
    canvas.save(Canvas.MATRIX_SAVE_FLAG);
    canvas.scale((float) getWidth(), (float) getHeight());

    canvas.drawBitmap(faces[face_to_show], null, new Rect(0, 0, 1, 1), null);

    canvas.restore();
  }

  /**
   * onTouchEvent()
   */
  public boolean onTouchEvent(MotionEvent motionEvent) {
    float eventY = motionEvent.getY();
    float viewHeight = getHeight();
    switch (motionEvent.getAction()) {
      case MotionEvent.ACTION_DOWN:  // indicate user touching screen?
        face_to_show = getSAFEValueByScreenPosition(eventY / viewHeight);
        invalidate();
        return true;
      case MotionEvent.ACTION_MOVE:
        face_to_show = getSAFEValueByScreenPosition(eventY / viewHeight);
        invalidate();
        return true;
      case MotionEvent.ACTION_UP:
        face_to_show = getSAFEValueByScreenPosition(eventY / viewHeight);
        invalidate();
        return true;
      default:
        return super.onTouchEvent(motionEvent);
    }
  }

  /**
   * SAFE should take up all available space.
   * TODO:philadams read xml attr for size
   */
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int widthDim = MeasureSpec.getSize(widthMeasureSpec);
    int heightDim = MeasureSpec.getSize(heightMeasureSpec);
    setMeasuredDimension(widthDim, heightDim);
  }

  private int getSAFEValueByScreenPosition(float position) {
    return 100 - Utility.clamp(Math.round(position * 100), 0, 100);
  }

  public int getProgress() {
    return face_to_show;
  }
}
