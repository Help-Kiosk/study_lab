package com.example.study_lab.dailychallenge;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.study_lab.R;

import java.io.IOException;

public class DailyChallengeFragment extends Fragment {
    private DailyChallengeViewModel dailyChallengeViewModel;
    private Classifier classifier;
    private TouchView touchView;
    private TextView predict_text;
    private Button btn_clear;
    private Button btn_detect;
    private Button btn_submit;
    private int output;

    public DailyChallengeFragment() {
    }

    public static DailyChallengeFragment newInstance(String param1, String param2) {
        DailyChallengeFragment fragment = new DailyChallengeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dailyChallengeViewModel = new ViewModelProvider(this).get(DailyChallengeViewModel.class);

        try {
            classifier = new Classifier(requireActivity());
        } catch (IOException e) {
            Toast.makeText(requireContext(), "fail to build classifier", Toast.LENGTH_SHORT).show();
            Log.e("MnistFragment", "init(): Failed to create Classifier", e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dailychallenge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        touchView = view.findViewById(R.id.touch_view);
        predict_text = view.findViewById(R.id.predict_text);
        btn_clear = view.findViewById(R.id.daily_btn_clear);
        btn_detect = view.findViewById(R.id.daily_btn_detect);
        btn_submit = view.findViewById(R.id.daily_btn_submit);

        btn_detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (touchView == null){
                    Toast.makeText(requireContext(), "숫자를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Bitmap image = touchView.exportToBitmap(28, 28);
                    output = classifier.classify(image);

                    predict_text.setText(String.valueOf(output));
                }
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchView.clear();
                predict_text.setText("--");
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (output<1 || output >6){
                    Toast.makeText(requireContext(), "올바른 숫자를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    dailyChallengeViewModel.loadAnswer();
                }
            }
        });

        dailyChallengeViewModel.getLoadedData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (output==integer){
                    NavHostFragment.findNavController(DailyChallengeFragment.this).navigate(R.id.action_dailyChallengeFragment_to_correctFragment);
                }
                else {
                    NavHostFragment.findNavController(DailyChallengeFragment.this).navigate(R.id.action_dailyChallengeFragment_to_wrongFragment);
                }
            }
        });

    }

}