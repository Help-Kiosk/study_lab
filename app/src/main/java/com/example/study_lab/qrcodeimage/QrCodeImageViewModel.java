package com.example.study_lab.qrcodeimage;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.study_lab.UserRepository;
import com.example.study_lab.model.Result;
import com.example.study_lab.model.User;

public class QrCodeImageViewModel extends ViewModel {
    private UserRepository userRepository = UserRepository.getInstance();
    private MutableLiveData<Boolean> isQrLoaded = new MutableLiveData<>(false);
    private Drawable qrCodeImage;

    private User currUser;

    public void setUser(String userId) {
        currUser = userRepository.getUser(userId);
        userRepository.loadQrDrawableForUser(userId, new UserRepository.UserRepositoryCallback<Result<Drawable>>() {
            @Override
            public void onComplete(Result<Drawable> drawableResult) {
                if (drawableResult instanceof Result.Success) {
                    qrCodeImage = ((Result.Success<Drawable>) drawableResult).getData();
                    isQrLoaded.postValue(true);
                } else {
                    //오류 보여주기
                }
            }
        });
    }

    public LiveData<Boolean> isQrImageLoaded() {
        return isQrLoaded;
    }

    public Drawable getQrCodeImage() {
        return qrCodeImage;
    }

    public User getCurrUser() {
        return currUser;
    }

}
