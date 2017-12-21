package com.thesim.simsimulator;


import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements SensorEventListener {

  private final String TAG = this.getClass().getSimpleName();

  private View contentView;

  private ImageView ivLogoBig;
  private TextView tvMessageBig;

  private SensorManager sensorManager;

  float[] accelerometerReading = new float[3];
  float[] magnetometerReading = new float[3];

  float[] rotation = new float[9];
  float[] inclination = new float[9];
  float[] orientation = new float[3];

  int clickCount = 0;
  boolean easterEggUnlocked;
  boolean darkThemeActivated;

  public MenuFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    contentView = inflater.inflate(R.layout.fragment_menu, container, false);

    easterEggUnlocked = false;
    darkThemeActivated = false;
    ivLogoBig = (ImageView) contentView.findViewById(R.id.main_logo);
    tvMessageBig = (TextView) contentView.findViewById(R.id.main_message);

    final DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
    tvMessageBig.setText(R.string.app_name_uppercase);

    tvMessageBig.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        clickCount += 1;
        if (clickCount % 5 == 0) {
          if (!easterEggUnlocked) {
            easterEggUnlocked = true;
            Toast.makeText(getActivity().getBaseContext(), "Easter Egg Unlocked!", Toast.LENGTH_SHORT).show();
          } else {
            RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(500);
            ivLogoBig.startAnimation(anim);
          }
          clickCount %= 5;
        }
      }
    });

    ivLogoBig.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        if (darkThemeActivated) {
          darkThemeActivated = false;
          contentView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
          Toast.makeText(getActivity().getBaseContext(), "Normal theme activated", Toast.LENGTH_SHORT).show();
        } else {
          darkThemeActivated = true;
          contentView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
          Toast.makeText(getActivity().getBaseContext(), "Dark mode for main menu activated", Toast.LENGTH_SHORT).show();
        }
        return true;
      }
    });

    ivLogoBig.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        drawer.openDrawer(Gravity.START);
      }
    });

    sensorManager = (SensorManager) getActivity().getSystemService(Activity.SENSOR_SERVICE);

    sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
        SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
    sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
        SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);

    return contentView;
  }

  @Override
  public void onSensorChanged(SensorEvent event) {

    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
      accelerometerReading = event.values;
    } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
      magnetometerReading = event.values;
    }

    sensorManager.getRotationMatrix(rotation, inclination, accelerometerReading, magnetometerReading);
    sensorManager.getOrientation(rotation, orientation);

    if (easterEggUnlocked) {
      ivLogoBig.animate()
          .rotationBy(accelerometerReading[0] / 2)
          .setInterpolator(new LinearInterpolator())
          .setDuration(150);
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int i) {

  }
}
