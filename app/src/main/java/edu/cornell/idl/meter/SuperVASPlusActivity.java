package edu.cornell.idl.meter;

public class SuperVASPlusActivity extends MeterBaseActivity {

  @Override
  protected int getLayoutResourceId() {
    return R.layout.activity_super_vas_plus;
  }

  @Override
  protected float getReportedScore() {
    SuperVASPlusView superVASPlusView = (SuperVASPlusView) findViewById(R.id.super_vas_plus_view);
    return (float) superVASPlusView.getProgress();
  }

  @Override
  protected String getMeterNameAndVersion() {
    return "SuperVASPlus v0.0.1";
  }

  @Override
  protected void resetView() {}
}