package edu.cornell.idl.meter;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.ArrayList;

public class PhotosLandscapesActivity extends MeterBaseActivity {

  ArrayList<ImageView> images = new ArrayList<ImageView>();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ViewGroup group = (ViewGroup) findViewById(R.id.photos);
    View v;

    // build up list of ImageView
    for (int i = 0; i < group.getChildCount(); i++) {
      v = group.getChildAt(i);
      if (v instanceof ImageView) {
        images.add((ImageView) v);
      }
    }

    // add onclick to each ImageView
    for (ImageView im : images) {
      im.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          for (ImageView img : images) {
            ((ImageView) img).setColorFilter(null);
          }
          ((ImageView) view).setColorFilter(0xffff9933, PorterDuff.Mode.MULTIPLY);
        }
      });
    }

  }

  @Override
  protected int getLayoutResourceId() {
    return R.layout.activity_photos_landscapes;
  }

  @Override
  protected float getReportedScore() {
    return -1;
  }

  @Override
  protected String getMeterNameAndVersion() {
    return "PhotosLandscapes v0.0.1";
  }

  @Override
  protected void resetView() {}
}
