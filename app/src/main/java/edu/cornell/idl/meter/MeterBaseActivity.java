package edu.cornell.idl.meter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public abstract class MeterBaseActivity extends Activity {

  final static String TAG = "MeterBaseActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    LayoutInflater layoutInflater = getLayoutInflater();
    View layout = layoutInflater.inflate(getLayoutResourceId(), null);
    setContentView(layout);

    Button submitButton = (Button) layout.findViewById(R.id.submit_button);
    submitButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        float reportedScore = getReportedScore();

        Intent result = new Intent();
        result.putExtra(MeterActivity.OHMAGE_SCORE, reportedScore);
        setResult(RESULT_OK, result);
        finish();
      }
    });
  }

  protected abstract int getLayoutResourceId();

  protected abstract float getReportedScore();

}
