package edu.cornell.idl.meter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MeterActivity extends Activity {

  static final String OHMAGE_SCORE = "score";
  static final int LAUNCH_METER_ONE = 1;
  static final int LAUNCH_VAS = 11;
  static final int LAUNCH_NRS = 12;
  static final int LAUNCH_SUURETA = 13;

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
    if (id == R.id.action_meter_one) {
      startActivityForResult(new Intent(this, MeterOneActivity.class), MeterActivity.LAUNCH_METER_ONE);
    }
    if (id == R.id.action_vas) {
      startActivityForResult(new Intent(this, VASActivity.class), MeterActivity.LAUNCH_VAS);
    }
    if (id == R.id.action_nrs) {
      startActivityForResult(new Intent(this, NRSActivity.class), MeterActivity.LAUNCH_NRS);
    }
    if (id == R.id.action_suureta) {
      startActivityForResult(new Intent(this, SuuretaActivity.class), MeterActivity.LAUNCH_SUURETA);
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      float reportedPainLevel = data.getFloatExtra(MeterActivity.OHMAGE_SCORE, -1);
      Toast.makeText(this, String.format("Reported pain level: %.0f", reportedPainLevel), Toast.LENGTH_SHORT).show();
    }
    //if (requestCode == MeterActivity.LAUNCH_VAS) {}
  }
}
