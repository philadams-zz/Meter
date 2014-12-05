package edu.cornell.idl.meter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import java.util.Random;

public class MeterOneActivity extends Activity {

  private Class meters[] = {
      ManyFingersActivity.class, NRSActivity.class, SuperVASActivity.class, SAFEActivity.class,
      SuperVASPlusActivity.class, SuuretaActivity.class, TapTapActivity.class
  };

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // launch a randomly selected meter, capture its response, and return data to calling activity
    Class meter = meters[(new Random()).nextInt(meters.length)];
    Intent intent = new Intent(this, meter);
    intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
    startActivity(intent);
    finish();
  }
}
