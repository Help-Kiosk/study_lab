package com.example.study_lab.QrImage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.study_lab.R;

public class QrImageFragment extends Fragment {
    private QrImageViewModel qrImageViewModel;
    private ImageView iv_qr;

    public QrImageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qrImageViewModel = new ViewModelProvider(this).get(QrImageViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        qrImageViewModel.setUser(QrImageFragmentArgs.fromBundle(getArguments()).getUserName());
        return inflater.inflate(R.layout.fragment_qrimage, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iv_qr = view.findViewById(R.id.qrimage_iv_qr);

        qrImageViewModel.isQrImageLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isQrLoaded) {
                if(isQrLoaded){
                    iv_qr.setImageDrawable(qrImageViewModel.getQrImage());
                }
            }
        });
    }
}