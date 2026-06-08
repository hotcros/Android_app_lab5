package com.korneev.fragmentlab2;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        TextView tvHistory = findViewById(R.id.text_view_history);

        try {
            FileInputStream fis = openFileInput("history.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

            if (sb.toString().isEmpty()) {
                tvHistory.setText("Сховище пусте");
            } else {
                tvHistory.setText(sb.toString());
            }

            fis.close();
        } catch (IOException e) {
            tvHistory.setText("Сховище пусте");
        }
    }
}