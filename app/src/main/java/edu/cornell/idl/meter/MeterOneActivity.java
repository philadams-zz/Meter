package edu.cornell.idl.meter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MeterOneActivity extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // launch a randomly selected meter, capture its response, and return data to calling activity
    Class meter = SuperVASNumberedActivity.class;
    Intent intent = new Intent(this, meter);
    intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
    startActivity(intent);
    finish();
  }
}
