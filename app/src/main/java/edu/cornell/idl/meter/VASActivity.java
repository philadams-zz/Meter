package edu.cornell.idl.meter;

public class VASActivity extends MeterBaseActivity {

  @Override
  protected int getLayoutResourceId() {
    return R.layout.activity_vas;
  }

  @Override
  protected float getReportedScore() {
    VerticalSeekBar verticalSeekBar = (VerticalSeekBar) findViewById(R.id.vas_vertical_seek_bar);
    return (float) verticalSeekBar.getProgress();
  }

  @Override
  protected String getMeterNameAndVersion() {
    return "VAS v0.0.1";
  }
}
