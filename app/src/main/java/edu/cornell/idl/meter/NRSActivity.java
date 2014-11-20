package edu.cornell.idl.meter;

import android.widget.RadioButton;
import android.widget.RadioGroup;

public class NRSActivity extends MeterBaseActivity {

  @Override
  protected int getLayoutResourceId() {
    return R.layout.activity_nrs;
  }

  @Override
  protected float getReportedScore() {
    float reportedScore = -1;
    RadioGroup radioGroup = (RadioGroup) findViewById(R.id.nrs_radiogroup);
    int selectedRadioButton = radioGroup.getCheckedRadioButtonId();
    if (reportedScore != -1) {
      reportedScore =
          Float.parseFloat(((RadioButton) findViewById(selectedRadioButton)).getText().toString());
    }
    return reportedScore;
  }

  @Override
  protected String getMeterNameAndVersion() {
    return "NRS11 v0.0.1";
  }

  @Override
  protected void resetView() {}
}