package com.example.android.camera2basic;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class CameraActivity extends Activity implements SensorEventListener {

    SensorManager sManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }
    }

    SensorEventListener sListener;

    //when this Activity starts
    @Override
    protected void onResume()
    {
        super.onResume();
    /*register the sensor listener to listen to the gyroscope sensor, use the
    callbacks defined in this class, and gather the sensor information as quick
    as possible*/

        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    float g[] = new float[3];
    float magnitude;
    int inclination;
    int rotation;


    @Override
    public void onSensorChanged(SensorEvent event)
    {
        g = event.values.clone();

        magnitude = (float) Math.sqrt(g[0]*g[0] + g[1]*g[1] + g[2]*g[2]);

        for(int i = 0; i < 3; i++) {
            g[i] = g[i]/magnitude;
        }

        inclination = (int) Math.round(Math.toDegrees(Math.acos(g[2])));

        if(inclination < 25 || inclination > 155) {
            // device is approximately flat
            TextView degreeText = (TextView) findViewById(R.id.azimuth_angle);
            degreeText.setText("Flat");
        }
        else {
            rotation = (int) Math.round(Math.toDegrees(Math.atan2(g[0], g[1])));
            TextView degreeText = (TextView) findViewById(R.id.azimuth_angle);
            degreeText.setText(String.valueOf(rotation));
        }



    }
}
