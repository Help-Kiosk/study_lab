package com.example.study_lab.dailychallenge;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.study_lab.UserRepository;
import com.example.study_lab.model.Result;
import com.google.firebase.firestore.DocumentSnapshot;

public class DailyChallengeViewModel extends ViewModel {
    private UserRepository userRepository = UserRepository.getInstance();
    private MutableLiveData<Integer> loadedData = new MutableLiveData<>();

    public void loadAnswer(){
        userRepository.loadAnswer(result ->{
            if (result instanceof Result.Success){
                DocumentSnapshot document = ((Result.Success<DocumentSnapshot>) result).getData();
                loadedData.setValue(Integer.valueOf((String) document.get("answer")));
            }
        });
    }

    public LiveData<Integer> getLoadedData() {
        return loadedData;
    }
}
