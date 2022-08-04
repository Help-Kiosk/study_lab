package com.example.study_lab.dailychallenge;

import android.app.Activity;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.study_lab.UserRepository;
import com.example.study_lab.model.Result;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.concurrent.Executor;

public class DailyChallengeViewModel extends ViewModel {
    private UserRepository userRepository = UserRepository.getInstance();
    private MutableLiveData<Integer> dataLoaded = new MutableLiveData<>();
    private MutableLiveData<Boolean> uriLoaded = new MutableLiveData<>();
    private Task<Uri> pathUri = new Task<Uri>() {
        @NonNull
        @Override
        public Task<Uri> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Uri> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Uri> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Uri> addOnSuccessListener(@NonNull OnSuccessListener<? super Uri> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Uri> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super Uri> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Uri> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super Uri> onSuccessListener) {
            return null;
        }

        @Nullable
        @Override
        public Exception getException() {
            return null;
        }

        @Override
        public Uri getResult() {
            return null;
        }

        @Override
        public <X extends Throwable> Uri getResult(@NonNull Class<X> aClass) throws X {
            return null;
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Override
        public boolean isComplete() {
            return false;
        }

        @Override
        public boolean isSuccessful() {
            return false;
        }
    };


    public void loadAnswer(){
        userRepository.loadAnswer(result ->{
            if (result instanceof Result.Success){
                DocumentSnapshot document = ((Result.Success<DocumentSnapshot>) result).getData();
                dataLoaded.setValue(Integer.valueOf((String) document.get("answer")));
            }
        });
    }

    public void loadQuestion(){
        userRepository.loadQuestion(result -> {
            if (result instanceof Result.Success){
                pathUri = ((Result.Success<Task>)result).getData();
                uriLoaded.setValue(true);
            }
        });

    }

    public LiveData<Integer> getDataLoaded() {
        return dataLoaded;
    }
    public LiveData<Boolean> isUriLoaded(){return uriLoaded;}
    public Task<Uri> getUri(){return pathUri;}
}
