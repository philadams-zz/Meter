package edu.cornell.idl.meter;

public class SAFESliderActivity extends MeterBaseActivity {

  @Override
  protected int getLayoutResourceId() {
    return R.layout.activity_safe_slider;
  }

  @Override
  protected float getReportedScore() {
    SAFESliderView safeSliderView = (SAFESliderView) findViewById(R.id.safe_slider_view);
    return (float) safeSliderView.getProgress();
  }

  @Override
  protected String getMeterNameAndVersion() {
    return "SAFESlider v0.0.1";
  }

  @Override
  protected void resetView() {}
}