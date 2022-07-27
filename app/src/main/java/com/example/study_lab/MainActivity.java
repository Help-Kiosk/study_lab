package com.example.study_lab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.study_lab.datasource.FirebaseDataSource;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDataSource ds = new FirebaseDataSource();
        UserRepository.getInstance().setDataSource(ds);
        setContentView(R.layout.activity_main);
    }
}