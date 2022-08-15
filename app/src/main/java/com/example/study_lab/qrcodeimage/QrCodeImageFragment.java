package com.example.study_lab.qrcodeimage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.study_lab.R;
import com.example.study_lab.databinding.FragmentQrcodeimageBinding;
import com.example.study_lab.model.User;

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

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.SEND_SMS}, 1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQrcodeimageBinding.inflate(inflater, container, false);
        iv_qrCodeImage = binding.qrcodeIvQrcode;
        qrCodeImageViewModel.setUser(QrCodeImageFragmentArgs.fromBundle(getArguments()).getUserId());
        qrCodeImageViewModel.getUserCheckInState(QrCodeImageFragmentArgs.fromBundle(getArguments()).getUserId());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        qrCodeImageViewModel.isQrImageLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoaded) {
                if (isLoaded) {
                    iv_qrCodeImage.setImageDrawable(qrCodeImageViewModel.getQrCodeImage());
                }
            }
        });

        qrCodeImageViewModel.isUserCheckedIn().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isUserCheckedIn) {
                if (isUserCheckedIn) {
                    User user = qrCodeImageViewModel.getCurrUser();
                    String msg = user.getName() + " 학생이 등원하였습니다.";

                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(user.getPhoneNumber(), null, msg, null, null);
                        Toast.makeText(getActivity().getApplicationContext(), "메시지 전송 완료", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), "메시지 전송 실패", Toast.LENGTH_SHORT).show();
                        Log.d("DEBUG", "onChanged: ");
                    }

                    qrCodeImageViewModel.changeCheckInState(QrCodeImageFragmentArgs.fromBundle(getArguments()).getUserId());
                    NavHostFragment.findNavController(QrCodeImageFragment.this)
                            .navigate(R.id.action_qrCodeImageFragment_to_dailyChallengeFragment);
                }
            }
        });
    }
}