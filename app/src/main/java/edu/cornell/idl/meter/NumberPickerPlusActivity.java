package edu.cornell.idl.meter;

/**
 * Essentially a Number Picker with giant selection targets and a giant number.
 * Also intended to show an 'out of X' interface, vs simply selecting a value.
 */
public class NumberPickerPlusActivity extends MeterBaseActivity {

  @Override
  protected int getLayoutResourceId() {
    return R.layout.activity_number_picker_plus;
  }

  @Override
  protected float getReportedScore() {
    NumberPickerPlusView view = (NumberPickerPlusView) findViewById(R.id.number_picker_plus_view);
    return (float) view.getProgress();
  }

  @Override
  protected String getMeterNameAndVersion() {
    return "NumberPickerPlusActivity v0.0.1";
  }

  @Override
  protected void resetView() {}
}
