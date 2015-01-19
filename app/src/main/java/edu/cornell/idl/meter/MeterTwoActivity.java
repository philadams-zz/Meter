package edu.cornell.idl.meter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MeterTwoActivity extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // we do it this way to enable changing Meter.app without needing to reconfigure the
    // Ohmage surveys (slightly annoying, but you work with what you have).

    // launch a randomly selected meter, capture its response, and return data to calling activity
    Class meter = SAFESliderActivity.class;
    Intent intent = new Intent(this, meter);
    intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
    startActivity(intent);
    finish();
  }
}
