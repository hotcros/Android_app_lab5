package com.korneev.fragmentlab2;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private Sensor proximitySensor;

    private TextView textLight;
    private TextView textProximity;
    private LinearLayout mainLayout;
    private LinearLayout screenOffLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textLight = findViewById(R.id.text_light);
        textProximity = findViewById(R.id.text_proximity);
        mainLayout = findViewById(R.id.main_layout);
        screenOffLayout = findViewById(R.id.screen_off_layout);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null) {
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }
        if (lightSensor == null) {
            Toast.makeText(this, "Датчик освітлення відсутній!", Toast.LENGTH_LONG).show();
        }
        if (proximitySensor == null) {
            Toast.makeText(this, "Датчик наближення відсутній!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (proximitySensor != null) {
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightValue = event.values[0];
            textLight.setText("Рівень освітлення: " + lightValue + " lx");
            if (lightValue < 50) {
                mainLayout.setBackgroundColor(Color.parseColor("#555555"));
                textLight.setTextColor(Color.WHITE);
                textProximity.setTextColor(Color.WHITE);
            } else {
                mainLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                textLight.setTextColor(Color.BLACK);
                textProximity.setTextColor(Color.BLACK);
            }
        }

        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float distance = event.values[0];
            textProximity.setText("Відстань до перешкоди: " + distance + " см");
            if (distance < proximitySensor.getMaximumRange()) {
                screenOffLayout.setVisibility(View.VISIBLE);
            } else {
                screenOffLayout.setVisibility(View.GONE);
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}