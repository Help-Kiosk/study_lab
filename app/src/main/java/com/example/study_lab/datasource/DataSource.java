package com.example.study_lab.datasource;

import com.example.study_lab.model.Result;

public interface DataSource {
    void tryRegister(String id, String password, String displayName, String phoneNum, String checkIn,DataSourceCallback<Result> callback);

    void tryLogin(String id, String password, DataSourceCallback<Result> callback);

    void getId(DataSourceCallback<Result> callback);

    void loadAnswer(DataSourceCallback<Result> callback);

    void loadQuestion(DataSourceCallback<Result> callback);

    void getUserCheckInState(String userId, ListenerCallback<Result<String>> callback);
}
