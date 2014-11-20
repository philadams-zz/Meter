package edu.cornell.idl.meter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * TODO force user to do *something* and not submit empty results - what's empty and what's 0?
 * TODO support resetting of each meter, ideally with a consistent method (longPress? doubletap?)
 * shake?)
 */
public abstract class MeterBaseActivity extends Activity {

  final static String TAG = "MeterBaseActivity";

  private long activityCreated;
  private View view;  // inflated by subclasses

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    activityCreated = System.nanoTime();

    LayoutInflater layoutInflater = getLayoutInflater();
    view = layoutInflater.inflate(getLayoutResourceId(), null);
    setContentView(view);

    Button resetButton = (Button) findViewById(R.id.reset_button);
    if (resetButton != null) {
      resetButton.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          resetView();
        }
      });
    }

    Button submitButton = (Button) view.findViewById(R.id.submit_button);
    submitButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        float reportedScore = getReportedScore();

        Intent result = new Intent();
        result.putExtra(Constants.OHMAGE_SCORE_KEY, reportedScore);
        result.putExtra("secondsToComplete", getCompletionSeconds());
        result.putExtra("meterNameAndVersion", getMeterNameAndVersion());
        setResult(RESULT_OK, result);
        finish();
      }
    });
  }

  private double getCompletionSeconds() {
    return ((double) (System.nanoTime() - activityCreated)) / Constants.ONE_BILLION;
  }

  protected abstract int getLayoutResourceId();

  protected abstract String getMeterNameAndVersion();

  protected abstract float getReportedScore();

  protected abstract void resetView();
}
