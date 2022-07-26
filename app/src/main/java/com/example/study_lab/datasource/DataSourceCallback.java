package com.example.study_lab.datasource;

public interface DataSourceCallback<Result> {
    void onComplete(Result result);
}