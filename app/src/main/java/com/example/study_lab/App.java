package com.example.study_lab;

import android.app.Application;


public class App extends Application {

    public static String getQrImagePath(String userId)
    {
        return "userQrImages/user_" + userId + ".jpg";
    }
}
