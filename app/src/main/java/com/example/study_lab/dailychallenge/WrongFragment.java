package com.example.study_lab.dailychallenge;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.study_lab.R;

public class WrongFragment extends Fragment {

    public WrongFragment() {
    }

    public static WrongFragment newInstance(String param1, String param2) {
        WrongFragment fragment = new WrongFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wrong, container, false);
    }
}