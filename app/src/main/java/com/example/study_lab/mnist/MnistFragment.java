package com.example.study_lab.mnist;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.study_lab.R;

import java.io.IOException;

public class MnistFragment extends Fragment {
    private Classifier classifier;

    private TouchView touchView;
    private TextView predict_text;
    private Button btn_clear;
    private Button btn_detect;

    public MnistFragment() {
    }

    public static MnistFragment newInstance(String param1, String param2) {
        MnistFragment fragment = new MnistFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        classifier = new Classifier(requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mnist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        touchView = view.findViewById(R.id.touch_view);
        predict_text = view.findViewById(R.id.predict_text);
        btn_clear = view.findViewById(R.id.btn_clear);
        btn_detect = view.findViewById(R.id.btn_detect);

        btn_detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (touchView == null){
                    Toast.makeText(requireContext(), "숫자를 입력해주세요.", Toast.LENGTH_SHORT);
                }

                Bitmap bitmap = touchView.exportToBitmap(28, 28);
                int output = classifier.classify(bitmap);

                predict_text.setText(String.valueOf(output));
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchView.clear();
                predict_text.setText("--");
            }
        });

    }

}