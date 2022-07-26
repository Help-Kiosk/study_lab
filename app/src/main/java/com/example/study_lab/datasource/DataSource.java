package com.example.study_lab.datasource;

import com.example.study_lab.model.Result;

public interface DataSource {
    void tryRegister(String id, String password, String displayName, String phoneNum, DataSourceCallback<Result> callback);

    void getId(DataSourceCallback<Result> callback);

    void tryLogin(String id, String password, DataSourceCallback<Result> callback);


}
