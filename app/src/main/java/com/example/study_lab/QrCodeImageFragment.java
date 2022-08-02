package com.example.study_lab;

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

import com.example.study_lab.databinding.FragmentQrcodeimageBinding;

public class QrCodeImageFragment extends Fragment {
    private FragmentQrcodeimageBinding binding;
    private QrCodeImageViewModel qrCodeImageViewModel;
    private ImageView iv_qrCodeImage;


    public QrCodeImageFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qrCodeImageViewModel = new ViewModelProvider(this).get(QrCodeImageViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQrcodeimageBinding.inflate(inflater, container, false);
        iv_qrCodeImage = binding.qrcodeIvQrcode;

        qrCodeImageViewModel.setUser(QrCodeImageFragmentArgs.fromBundle(getArguments()).getUserId());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        qrCodeImageViewModel.isQrImageLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoaded) {
                if(isLoaded){
                    iv_qrCodeImage.setImageDrawable(qrCodeImageViewModel.getQrCodeImage());
                }
            }
        });
    }
}