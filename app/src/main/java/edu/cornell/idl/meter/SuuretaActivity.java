package edu.cornell.idl.meter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

// TODO:phil make circle stop growing at some max value
// TODO:phil make a reset button. or have two fingers shrink it.
public class SuuretaActivity extends Activity {

  final static String TAG = "SuuretaActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_suureta);

    Button submitButton = (Button) findViewById(R.id.suureta_button_submit);
    submitButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        SuuretaCircleView circleView = (SuuretaCircleView) findViewById(R.id.suureta_circle_view);
        float reportedValue = (float) circleView.getRadius();

        Log.d(TAG, String.format("reported value: %.0f/???", reportedValue));
        Intent result = new Intent();
        result.putExtra(MeterActivity.OHMAGE_SCORE, reportedValue);
        setResult(RESULT_OK, result);
        finish();
      }
    });
  }

}
