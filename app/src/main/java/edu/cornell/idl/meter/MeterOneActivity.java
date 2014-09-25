package edu.cornell.idl.meter;

import android.app.Activity;
import android.os.Bundle;

public class MeterOneActivity extends Activity {

  final static String TAG = "VASActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_vas);
  }

}