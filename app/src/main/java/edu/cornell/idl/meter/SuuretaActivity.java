package edu.cornell.idl.meter;

public class SuuretaActivity extends MeterBaseActivity {

  @Override
  protected int getLayoutResourceId() {
    return R.layout.activity_suureta;
  }

  @Override
  protected float getReportedScore() {
    SuuretaView suuretaView = (SuuretaView) findViewById(R.id.suureta_view);
    return (float) suuretaView.getProgress();
  }

}