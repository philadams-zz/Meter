package edu.cornell.idl.meter;

public class SuperVASActivity extends MeterBaseActivity {

  @Override
  protected int getLayoutResourceId() {
    return R.layout.activity_super_vas;
  }

  @Override
  protected float getReportedScore() {
    SuperVASView superVASView = (SuperVASView) findViewById(R.id.super_vas_view);
    return (float) superVASView.getProgress();
  }

}