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

  @Override
  protected void resetView() {
    SuuretaView suuretaView = (SuuretaView) findViewById(R.id.suureta_view);
    suuretaView.reset();
  }

  @Override
  protected String getMeterNameAndVersion() {
    return "Suureta v0.0.2";
  }
}