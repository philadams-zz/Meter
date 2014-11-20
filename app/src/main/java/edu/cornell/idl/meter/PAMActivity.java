package edu.cornell.idl.meter;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import java.io.IOException;
import java.util.Random;

/**
 * PAM activity for Meter.
 */
public class PAMActivity extends MeterBaseActivity {

  private Button moreImagesButton;
  private GridView gridView;

  private Random random = new Random();

  private String pam_photo_id;
  private Bitmap[] images;
  private int[] imageIds;
  private int selection = GridView.INVALID_POSITION;
  public static final String[] IMAGE_FOLDERS = new String[] {
      "1_afraid", "2_tense", "3_excited", "4_delighted", "5_frustrated", "6_angry", "7_happy",
      "8_glad", "9_miserable", "10_sad", "11_calm", "12_satisfied", "13_gloomy", "14_tired",
      "15_sleepy", "16_serene"
  };

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // set up more images button
    moreImagesButton = (Button) findViewById(R.id.pam_more_images);
    moreImagesButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        loadImages();
        setupPAM();
      }
    });

    gridView = (GridView) findViewById(R.id.pam_grid_view);

    loadImages();
    setupPAM();
  }

  /**
   * load images from PAM assets,
   * randomly selecting 1 image from each emotion folder
   */
  private void loadImages() {
    selection = GridView.INVALID_POSITION;
    images = new Bitmap[IMAGE_FOLDERS.length];
    imageIds = new int[IMAGE_FOLDERS.length];

    AssetManager assetManager = getResources().getAssets();
    String subfolder;
    for (int i = 0; i < IMAGE_FOLDERS.length; i++) {
      subfolder = "pam_images/" + IMAGE_FOLDERS[i];
      try {
        String filename = assetManager.list(subfolder)[random.nextInt(3)];
        images[i] = BitmapFactory.decodeStream(assetManager.open(subfolder + "/" + filename));
        imageIds[i] = filename.split("_")[1].charAt(0) - '0';
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * configure gridview with adapter and current set of Bitmap[] images
   */
  private void setupPAM() {
    gridView.setAdapter(new BaseAdapter() {

      @Override public int getCount() {
        return images.length;
      }

      @Override public Object getItem(int i) {
        return null;
      }

      @Override public long getItemId(int i) {
        return 0;
      }

      @Override public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (null == convertView) {
          imageView = new ImageView(getApplicationContext());
          imageView.setLayoutParams(
              new GridView.LayoutParams(parent.getWidth() / 4, parent.getWidth() / 4));
          imageView.setScaleType(ImageView.ScaleType.FIT_XY);
          imageView.setColorFilter(null);
        } else {
          imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(images[position]);

        if (position == selection) {
          highlightSelection(imageView);
        }

        return imageView;
      }
    });

    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (selection != GridView.INVALID_POSITION) {
          ((ImageView) parent.getChildAt(selection)).setColorFilter(null);
        }
        highlightSelection(view);
        selection = position;
        pam_photo_id = IMAGE_FOLDERS[position];
      }
    });
  }

  protected void highlightSelection(View view) {
    ((ImageView) view).setColorFilter(0xffff9933, PorterDuff.Mode.MULTIPLY);
  }

  @Override
  protected int getLayoutResourceId() {
    return R.layout.activity_pam;
  }

  @Override
  protected float getReportedScore() {
    return (selection != GridView.INVALID_POSITION) ? (float) selection : -1.0f;
  }

  @Override
  protected String getMeterNameAndVersion() {
    return "PAM v0.0.1";
  }

  @Override
  protected void resetView() {}
}
