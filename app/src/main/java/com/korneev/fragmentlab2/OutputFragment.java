package com.korneev.fragmentlab2;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
public class OutputFragment extends Fragment {
    private SharedViewModel viewModel;
    private TextView resultText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_output, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        resultText = view.findViewById(R.id.text_view_result);
        Button btnCancel = view.findViewById(R.id.button_cancel);
        viewModel.getText().observe(getViewLifecycleOwner(), s -> resultText.setText(s));
        viewModel.getColor().observe(getViewLifecycleOwner(), color -> resultText.setTextColor(color));
        btnCancel.setOnClickListener(v -> viewModel.requestClear());
        return view;
    }
}