package com.example.study_lab;

import android.app.Application;


public class App extends Application {
    private static FileService fileService;

    private static final UserRepository WORKSITE_REPOSITORY = UserRepository.getInstance();

    public static String getWorksiteQrImagePath(String userId)
    {
        return "userQrImages/user_" + userId + ".jpg";
    }
}
