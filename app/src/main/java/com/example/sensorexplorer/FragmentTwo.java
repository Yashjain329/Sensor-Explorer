package com.example.sensorexplorer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.Locale;

public class FragmentTwo extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer, gyroscope, magnetometer, lightSensor;

    private float[] accValues = new float[3];
    private float[] gyroValues = new float[3];
    private float[] magValues = new float[3];
    private float lightValue;

    private TextView basicSensorData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmenttwo, container, false);

        basicSensorData = view.findViewById(R.id.basic_sensor_data);

        // Initialize sensors
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (accelerometer != null)
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        if (gyroscope != null)
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        if (magnetometer != null)
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        if (lightSensor != null)
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                accValues = event.values.clone();
                break;
            case Sensor.TYPE_GYROSCOPE:
                gyroValues = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magValues = event.values.clone();
                break;
            case Sensor.TYPE_LIGHT:
                lightValue = event.values[0];
                break;
        }

        // Update UI
        if (basicSensorData != null) {
            String sensorInfo = String.format(Locale.US,
                    "Accelerometer:\nX: %.2f, Y: %.2f, Z: %.2f\n\n" +
                            "Gyroscope:\nX: %.2f, Y: %.2f, Z: %.2f\n\n" +
                            "Magnetometer:\nX: %.2f, Y: %.2f, Z: %.2f\n\n" +
                            "Light Sensor: %.2f lux",
                    accValues[0], accValues[1], accValues[2],
                    gyroValues[0], gyroValues[1], gyroValues[2],
                    magValues[0], magValues[1], magValues[2],
                    lightValue);

            basicSensorData.setText(sensorInfo);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed
    }
}
