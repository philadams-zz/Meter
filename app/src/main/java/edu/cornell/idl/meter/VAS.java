package edu.cornell.idl.meter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class VAS extends Activity {

  final static String TAG = "VAS";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_vas);

    Button submitButton = (Button) findViewById(R.id.vas_button_submit);
    submitButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        VerticalSeekBar seekBar = (VerticalSeekBar) findViewById(R.id.verticalseekbar);
        float reportedValue = (float) seekBar.getProgress();

        Log.d(TAG, String.format("reported value: %.0f/100", reportedValue));
        Intent result = new Intent();
        result.putExtra(Meter.OHMAGE_SCORE, reportedValue);
        setResult(RESULT_OK, result);
        finish();
      }
    });
  }

}
