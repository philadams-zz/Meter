package edu.cornell.idl.meter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class NRSActivity extends Activity {

  final static String TAG = "NRSActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_nrs);

    Button submitButton = (Button) findViewById(R.id.nrs_button_submit);
    submitButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.nrs_radiogroup);
        int selectedRadioButton = radioGroup.getCheckedRadioButtonId();
        float reportedValue = Float.parseFloat(
            ((RadioButton) findViewById(selectedRadioButton)).getText().toString());

        Log.d(TAG, String.format("reported value: %.0f/11", reportedValue));
        Intent result = new Intent();
        result.putExtra(MeterActivity.OHMAGE_SCORE, reportedValue);
        setResult(RESULT_OK, result);
        finish();
      }
    });
  }

}