package com.example.study_lab.datasource;

import com.example.study_lab.model.Result;

public interface DataSource {
    void tryRegister(String id, String password, String displayName, String phoneNum, String checkIn,DataSourceCallback<Result> callback);

    void tryLogin(String id, String password, DataSourceCallback<Result> callback);

    void getAllUsersId(DataSourceCallback<Result> callback);

    void getAnswer(DataSourceCallback<Result> callback);

    void getUserInformation(String id, DataSourceCallback<Result> callback);

    void getUserCheckInState(String id, ListenerCallback<Result<String>> callback);

    void changeCheckInState(String id, DataSourceCallback<Result> callback);
}
