package edu.cornell.idl.meter;

public class SAFEActivity extends MeterBaseActivity {

  @Override
  protected int getLayoutResourceId() {
    return R.layout.activity_safe;
  }

  @Override
  protected float getReportedScore() {
    SAFEView SAFEView = (SAFEView) findViewById(R.id.safe_view);
    return (float) SAFEView.getProgress();
  }

  @Override
  protected String getMeterNameAndVersion() {
    return "SAFE v0.0.1";
  }

  @Override
  protected void resetView() {}
}