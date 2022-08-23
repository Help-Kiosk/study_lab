package com.example.study_lab.dailychallenge;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.study_lab.R;

import java.io.IOException;

public class DailyChallengeFragment extends Fragment {
    private DailyChallengeViewModel dailyChallengeViewModel;
    private Classifier classifier;

    private TouchView touchView;
    private TextView tv_predict;
    private Button btn_clear;
    private Button btn_detect;
    private Button btn_submit;
    private ImageView iv_answer;

    private int output;
    private long pressedTime = 0;

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

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (System.currentTimeMillis() > pressedTime + 2000) {
                    pressedTime = System.currentTimeMillis();
                    Toast.makeText(requireContext(), R.string.tst_pressOneMoreToExit, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), R.string.tst_exitTheApp, Toast.LENGTH_SHORT).show();
                    requireActivity().finish();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        dailyChallengeViewModel.loadQuestion();

        try {
            classifier = new Classifier(requireActivity());
        } catch (IOException e) {
            Toast.makeText(requireContext(), R.string.tst_failToBuildClassifier, Toast.LENGTH_SHORT).show();
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

        touchView = view.findViewById(R.id.daily_touchView);
        tv_predict = view.findViewById(R.id.daily_tv_predict);
        btn_clear = view.findViewById(R.id.daily_btn_clear);
        btn_detect = view.findViewById(R.id.daily_btn_detect);
        btn_submit = view.findViewById(R.id.daily_btn_submit);
        iv_answer = view.findViewById(R.id.daily_iv_answer);

        btn_detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (touchView == null) {
                    Toast.makeText(requireContext(), R.string.tst_drawAnswer, Toast.LENGTH_SHORT).show();
                } else {
                    Bitmap image = touchView.exportToBitmap(28, 28);
                    output = classifier.classify(image);

                    tv_predict.setText(String.valueOf(output));
                }
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchView.clear();
                tv_predict.setText("--");
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (output < 1 || output > 6) {
                    Toast.makeText(requireContext(), R.string.tst_drawRightOne, Toast.LENGTH_SHORT).show();
                } else {
                    dailyChallengeViewModel.getAnswer();
                }
            }
        });

        dailyChallengeViewModel.getDataLoaded().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (output == integer) {
                    NavHostFragment.findNavController(DailyChallengeFragment.this).navigate(R.id.action_dailyChallengeFragment_to_correctFragment);
                } else {
                    NavHostFragment.findNavController(DailyChallengeFragment.this).navigate(R.id.action_dailyChallengeFragment_to_wrongFragment);
                }
            }
        });

        dailyChallengeViewModel.isUriLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoaded) {
                if (isLoaded) {
                    iv_answer.setImageDrawable(dailyChallengeViewModel.getAnswerImage());
                }
            }
        });
    }
}