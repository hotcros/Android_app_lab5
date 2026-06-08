package com.korneev.fragmentlab2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import java.io.FileOutputStream;
import java.io.IOException;

public class InputFragment extends Fragment {
    private SharedViewModel viewModel;
    private EditText editText;
    private RadioGroup radioGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        editText = view.findViewById(R.id.edit_text_input);
        radioGroup = view.findViewById(R.id.radio_group_colors);
        Button btnOk = view.findViewById(R.id.button_ok);
        Button btnOpen = view.findViewById(R.id.button_open); // Нова кнопка

        btnOk.setOnClickListener(v -> {
            String inputText = editText.getText().toString().trim();
            int checkedId = radioGroup.getCheckedRadioButtonId();

            if (inputText.isEmpty() || checkedId == -1) {
                Toast.makeText(requireContext(), "Будь ласка, введіть текст та оберіть колір!", Toast.LENGTH_SHORT).show();
            } else {
                int selectedColor = Color.BLACK;
                String colorName = "Чорний";

                if (checkedId == R.id.radio_red) {
                    selectedColor = Color.RED;
                    colorName = "Червоний";
                } else if (checkedId == R.id.radio_green) {
                    selectedColor = Color.GREEN;
                    colorName = "Зелений";
                } else if (checkedId == R.id.radio_blue) {
                    selectedColor = Color.BLUE;
                    colorName = "Синій";
                }
                viewModel.setData(inputText, selectedColor);
                String record = "Текст: " + inputText + " | Колір: " + colorName + "\n";
                try {
                    FileOutputStream fos = requireContext().openFileOutput("history.txt", Context.MODE_APPEND);
                    fos.write(record.getBytes());
                    fos.close();
                    Toast.makeText(requireContext(), "Дані успішно збережено!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(requireContext(), "Помилка збереження", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        btnOpen.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), HistoryActivity.class);
            startActivity(intent);
        });
        viewModel.getClearTrigger().observe(getViewLifecycleOwner(), clear -> {
            if (clear) {
                editText.setText("");
                radioGroup.clearCheck();
            }
        });
        return view;
    }
}