package com.example.study_lab.qrcodeimage;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.study_lab.UserRepository;
import com.example.study_lab.model.Result;
import com.example.study_lab.model.User;
import com.google.firebase.firestore.DocumentSnapshot;

public class QrCodeImageViewModel extends ViewModel {
    private UserRepository userRepository = UserRepository.getInstance();

    private MutableLiveData<Boolean> qrImageLoaded = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> qrLoaded = new MutableLiveData<>(false);

    private Drawable qrCodeImage;
    private User currUser;

    public void setUser(String id) {
        currUser = userRepository.getUser(id);
        userRepository.loadQrDrawableForUser(id, new UserRepository.UserRepositoryCallback<Result<Drawable>>() {
            @Override
            public void onComplete(Result<Drawable> drawableResult) {
                if (drawableResult instanceof Result.Success) {
                    qrCodeImage = ((Result.Success<Drawable>) drawableResult).getData();
                    qrImageLoaded.postValue(true);
                }
            }
        });
    }

    public void getUserCheckInState(String id) {
        userRepository.getUserCheckInState(id);
    }

    public void changeCheckInState(String id) {
        userRepository.changeCheckInState(id, result -> {
            if (result instanceof Result.Error) {
                Log.d("DEBUG", "changeCheckInState: fail to change checkIn state");
            }
        });
    }

    public LiveData<Boolean> isUserCheckedIn() {
        return userRepository.isUserCheckedIn();
    }

    public LiveData<Boolean> isQrImageLoaded() {
        return qrImageLoaded;
    }

    public Drawable getQrCodeImage() {
        return qrCodeImage;
    }

    public User getCurrUser() {
        return currUser;
    }

}
