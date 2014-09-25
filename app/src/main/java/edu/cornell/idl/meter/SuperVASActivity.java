package edu.cornell.idl.meter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SuperVASActivity extends Activity {

  final static String TAG = "SuperVASActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_super_vas);

    Button submitButton = (Button) findViewById(R.id.super_vas_button_submit);
    submitButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        SuperVASView superVAS = (SuperVASView) findViewById(R.id.super_vas_view);
        float reportedValue = (float) superVAS.getProgress();

        Log.d(TAG, String.format("reported value: %.0f/100", reportedValue));
        Intent result = new Intent();
        result.putExtra(MeterActivity.OHMAGE_SCORE, reportedValue);
        setResult(RESULT_OK, result);
        finish();
      }
    });
  }

}
