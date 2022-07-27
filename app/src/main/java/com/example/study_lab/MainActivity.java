package com.example.study_lab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.example.study_lab.datasource.FirebaseDataSource;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    FileService fileService;
    UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        FirebaseDataSource ds = new FirebaseDataSource();
        userRepository = UserRepository.getInstance();
        userRepository.setDataSource(ds);
        userRepository.setExecutor(executorService);

        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                FileService.LocalBinder binder = (FileService.LocalBinder) service;
                fileService = binder.getService();
                fileService.setFirebaseDataSource(ds);
                userRepository.setFileService(fileService);
                fileService.setExecutor(executorService);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        Intent intent = new Intent(this,FileService.class);
        startService(intent);
        bindService(intent,connection, Context.BIND_AUTO_CREATE);


        setContentView(R.layout.activity_main);
    }
}