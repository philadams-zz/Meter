package edu.cornell.idl.meter;

public class SuperVASNumberedActivity extends MeterBaseActivity {

  @Override
  protected int getLayoutResourceId() {
    return R.layout.activity_supervas_numbered;
  }

  @Override
  protected float getReportedScore() {
    SuperVASNumberedView superVASNumberedView = (SuperVASNumberedView) findViewById(R.id.supervas_numbered_view);
    return (float) superVASNumberedView.getProgress();
  }

  @Override
  protected String getMeterNameAndVersion() {
    return "SuperVASNumbered v0.0.1";
  }

  @Override
  protected void resetView() {}
}