package com.example.study_lab;

import com.example.study_lab.datasource.DataSource;
import com.example.study_lab.model.Result;

public class UserRepository {
    private static volatile UserRepository INSTANCE = new UserRepository();
    private DataSource dataSource;
    private String loggedInUserId;

    public static UserRepository getInstance() {
        return INSTANCE;
    }

    public void tryRegister(final String id, final String password, final String displayName, final String phoneNum, UserRepositoryCallback<Result> callback) {
        dataSource.tryRegister(id, password, displayName, phoneNum, callback::onComplete);
    }

    public void getId(final UserRepositoryCallback<Result> callback) {
        dataSource.getId(callback::onComplete);
    }

    public void tryLogin(final String id, final String password, UserRepositoryCallback<Result> callback) {
        dataSource.tryLogin(id, password, callback::onComplete);
    }

    public void saveRepositoryUserId(final String userId) {
        loggedInUserId = userId;
    }

    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
    }

    public interface UserRepositoryCallback<Result> {
        void onComplete(Result result);
    }
}
