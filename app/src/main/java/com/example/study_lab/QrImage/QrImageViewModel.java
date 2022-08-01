package com.example.study_lab.QrImage;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.study_lab.UserRepository;
import com.example.study_lab.model.Result;
import com.example.study_lab.model.User;

public class QrImageViewModel extends ViewModel {
    private UserRepository userRepository = UserRepository.getInstance();
    private MutableLiveData<Boolean> isQrLoaded = new MutableLiveData<>(false);
    private Drawable qrImage;

    private User currUser;

    public void setUser(String user) {
        currUser = userRepository.getUser(user);

        userRepository.loadQrDrawableForWorksite(user, new UserRepository.UserRepositoryCallback<Result<Drawable>>() {
            @Override
            public void onComplete(Result<Drawable> drawableResult) {
                if (drawableResult instanceof Result.Success) {
                    qrImage = ((Result.Success<Drawable>) drawableResult).getData();
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

    public Drawable getQrImage() {
        return qrImage;
    }

    public User getCurrUser() {
        return currUser;
    }

}
