package com.example.sensorexplorer;

import android.annotation.SuppressLint;
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

public class FragmentThree extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer, gyroscope, magnetometer, lightSensor;

    private TextView sensorDetailsText;

    private float[] accValues = new float[3];
    private float[] gyroValues = new float[3];
    private float[] magValues = new float[3];
    private float lightValue;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        sensorDetailsText = view.findViewById(R.id.sensor_list_text);

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        showSensorInfo(); // Display static info first

        return view;
    }

    private void showSensorInfo() {
        StringBuilder details = new StringBuilder();

        appendSensorDetails(details, accelerometer, "Accelerometer");
        appendSensorDetails(details, gyroscope, "Gyroscope");
        appendSensorDetails(details, magnetometer, "Magnetometer");
        appendSensorDetails(details, lightSensor, "Light Sensor");

        sensorDetailsText.setText(details.toString());
    }

    private void appendSensorDetails(StringBuilder sb, Sensor sensor, String name) {
        if (sensor == null) {
            sb.append(String.format("%s: Not available\n\n", name));
            return;
        }

        sb.append(String.format(Locale.US,
                "%s\n" +
                        "Name: %s\n" +
                        "Vendor: %s\n" +
                        "Version: %d\n" +
                        "Power: %.2f mA\n" +
                        "Resolution: %.5f\n" +
                        "Max Range: %.2f\n\n",
                name,
                sensor.getName(),
                sensor.getVendor(),
                sensor.getVersion(),
                sensor.getPower(),
                sensor.getResolution(),
                sensor.getMaximumRange()));
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

        // Add live values to the existing info
        StringBuilder updatedText = new StringBuilder();
        updatedText.append(sensorDetailsText.getText().toString());
        updatedText.append(String.format(Locale.US,
                "\nLive Sensor Values:\n" +
                        "Accelerometer: X=%.2f, Y=%.2f, Z=%.2f\n" +
                        "Gyroscope: X=%.2f, Y=%.2f, Z=%.2f\n" +
                        "Magnetometer: X=%.2f, Y=%.2f, Z=%.2f\n" +
                        "Light Sensor: %.2f lux\n",
                accValues[0], accValues[1], accValues[2],
                gyroValues[0], gyroValues[1], gyroValues[2],
                magValues[0], magValues[1], magValues[2],
                lightValue));

        sensorDetailsText.setText(updatedText.toString());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No-op
    }
}
