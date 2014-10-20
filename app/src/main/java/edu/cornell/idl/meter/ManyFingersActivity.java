package edu.cornell.idl.meter;

public class ManyFingersActivity extends MeterBaseActivity {

  @Override
  protected int getLayoutResourceId() {
    return R.layout.activity_many_fingers;
  }

  @Override
  protected float getReportedScore() {
    ManyFingersView manyFingersView = (ManyFingersView) findViewById(R.id.many_fingers_view);
    return (float) manyFingersView.getProgress();
  }

  @Override
  protected String getMeterNameAndVersion() {
    return "ManyFingers v0.0.1";
  }

}
