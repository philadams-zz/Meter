package edu.cornell.idl.meter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Meter extends Activity {

  static final String OHMAGE_SCORE = "score";
  static final int LAUNCH_VAS = 1;
  static final int LAUNCH_NRS = 2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_meter);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.meter, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_vas) {
      startActivityForResult(new Intent(this, VAS.class), Meter.LAUNCH_VAS);
    }
    if (id == R.id.action_nrs) {
      startActivityForResult(new Intent(this, NRS.class), Meter.LAUNCH_NRS);
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      float reportedPainLevel = data.getFloatExtra(Meter.OHMAGE_SCORE, -1);
      Toast.makeText(this, String.format("Reported pain level: %.0f", reportedPainLevel), Toast.LENGTH_SHORT).show();
    }
    //if (requestCode == Meter.LAUNCH_VAS) {}
  }
}
