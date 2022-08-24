package com.example.study_lab.dailychallenge;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.study_lab.UserRepository;
import com.example.study_lab.model.Result;
import com.google.firebase.firestore.DocumentSnapshot;

public class DailyChallengeViewModel extends ViewModel {
    private UserRepository userRepository = UserRepository.getInstance();

    private MutableLiveData<Integer> dataLoaded = new MutableLiveData<>();
    private MutableLiveData<Boolean> uriLoaded = new MutableLiveData<>();
    private Drawable questionImage;

    public void getAnswer() {
        userRepository.getAnswer(result -> {
            if (result instanceof Result.Success) {
                DocumentSnapshot document = ((Result.Success<DocumentSnapshot>) result).getData();
                dataLoaded.setValue(Integer.valueOf((String) document.get("answer")));
            }
        });
    }

    public void loadQuestion() {
        userRepository.loadQuestion(result -> {
            if (result instanceof Result.Success) {
                questionImage = ((Result.Success<Drawable>) result).getData();
                uriLoaded.postValue(true);
            } else {
                Log.d("DEBUG", "loadQuestion:  fail to get image");
            }
        });

    }

    public LiveData<Integer> getDataLoaded() {
        return dataLoaded;
    }

    public LiveData<Boolean> isUriLoaded() {
        return uriLoaded;
    }

    public Drawable getQuestionImage() {
        return questionImage;
    }
}
