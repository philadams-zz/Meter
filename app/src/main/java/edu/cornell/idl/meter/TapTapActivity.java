package edu.cornell.idl.meter;

public class TapTapActivity extends MeterBaseActivity {

  @Override
  protected int getLayoutResourceId() {
    return R.layout.activity_tap_tap;
  }

  @Override
  protected float getReportedScore() {
    TapTapView tapTapView = (TapTapView) findViewById(R.id.tap_tap_view);
    return (float) tapTapView.getProgress();
  }

}
