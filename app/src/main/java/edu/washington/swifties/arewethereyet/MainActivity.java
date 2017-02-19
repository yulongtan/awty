package edu.washington.swifties.arewethereyet;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

  private EditText messageEditText, phoneEditText, intervalEditText;
  private Button startStopButton;
  private boolean fieldsAreFilled = true;

  public static final String PHONE_NUMBER = "phone_number";

  private PendingIntent pi;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    messageEditText = (EditText) findViewById(R.id.messageEditText);
    phoneEditText = (EditText) findViewById(R.id.phoneEditText);
    intervalEditText = (EditText) findViewById(R.id.intervalEditText);

    final Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
    pi = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);

    startStopButton = (Button) findViewById(R.id.startStopButton);

    startStopButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // Checks if all the fields are filled out
        if (messageEditText.getText().toString().length() == 0 ||
            phoneEditText.getText().toString().length() == 0 ||
            intervalEditText.getText().toString().length() == 0) {
          fieldsAreFilled = false;
        } else {
          fieldsAreFilled = true;
        }

        // Tells user that fields are not filled
        if (!fieldsAreFilled) {
          Toast.makeText(MainActivity.this, "Cannot start: Not all fields are filled",
              Toast.LENGTH_SHORT).show();
        }

        Log.d("start", "Message length: " + messageEditText.getText().toString().length());
        Log.d("start", "Phone length: " + phoneEditText.getText().toString().length());
        Log.d("start", "interval length: " + messageEditText.getText().toString().length());

        // Do something if all the fields are filled up
        if (fieldsAreFilled) {
          // Passes the user given phone number
          alarmIntent.putExtra(PHONE_NUMBER, phoneEditText.getText().toString());

          // Starts the alarm service
          // When start is clicked, the text shows stop and vice versa
          // Start text is green and cancel text is red
          if (startStopButton.getText().toString().equals("START")) {
            startStopButton.setText("CANCEL");
            startStopButton.setTextColor(Color.RED);
            startAlarm();
          } else {
            startStopButton.setText("START");
            startStopButton.setTextColor(Color.GREEN);
            stopAlarm();
          }
        }
      }
    });
  }

  // Begins the alarm manager with the user given interval
  private void startAlarm() {
    AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
        Integer.parseInt(intervalEditText.getText().toString()), pi);
  }

  // Cancels the alarm (so it stops running in the background as well)
  private void stopAlarm() {
    AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    manager.cancel(pi);
  }
}
