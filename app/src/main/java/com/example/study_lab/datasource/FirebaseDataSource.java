package com.example.study_lab.datasource;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.study_lab.model.Result;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseDataSource implements DataSource {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    @Override
    public void tryRegister(String id, String password, String displayName, String phoneNum, DataSourceCallback<Result> callback) {
        Map<String, String> user = new HashMap<>();
        user.put("id", id);
        user.put("password", password);
        user.put("displayName", displayName);
        user.put("phoneNum", phoneNum);

        db.collection("users")
                .document(id)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("datasource", "onSuccess: firestore finish");
                        callback.onComplete(new Result.Success<String>("Success"));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("datasource", "onSuccess: firestore not finish");
                        callback.onComplete(new Result.Error(new Exception("Failed")));
                    }
                });
    }

    @Override
    public void getId(DataSourceCallback<Result> callback) {
        List<String> toReturn = new ArrayList<>();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> snaps = task.getResult().getDocuments();
                            for (int i = 0; i < snaps.size(); i++) {
                                String toAdd = new String((snaps.get(i).getString("id")));
                                toReturn.add(toAdd);
                            }
                            callback.onComplete(new Result.Success<List<String>>(toReturn));
                        } else {
                            callback.onComplete(new Result.Error(task.getException()));
                        }
                    }
                });
    }

    @Override
    public void tryLogin(String id, String password, DataSourceCallback<Result> callback) {
        db.collection("users")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                if (document.get("password").equals(password)) {
                                    callback.onComplete(new Result.Success<String>("Success"));
                                } else {
                                    callback.onComplete(new Result.Error(new Exception("Failed")));
                                }
                            } else {
                                callback.onComplete(new Result.Error(new Exception("Failed")));
                            }
                        } else {
                            callback.onComplete(new Result.Error(new Exception("Failed")));
                        }
                    }
                });
    }

    public void uploadFile(File toUpload, String destination, DataSourceCallback<Result<Uri>> callback) {
        Log.d("DEBUG:DataSource", "uploadFile: " + toUpload.getName() + " to " + destination);
        Uri localFile = Uri.fromFile(toUpload);
        StorageReference storageReference = firebaseStorage.getReference().child(destination);

        storageReference.putFile(localFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri result = task.getResult();
                            callback.onComplete(new Result.Success<Uri>(result));
                        } else {

                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onComplete(new Result.Error(e));
                Log.d("DEBUG", "DataSource: storeImage() failed!");
            }
        });
    }

    public void downloadFile(String downloadPath, File localFile, DataSourceCallback<Result> callback) {
        Log.d("DEBUG:DataSource", "downloadFile: " + downloadPath);
        StorageReference ref = firebaseStorage.getReference().child(downloadPath);

        ref.getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    callback.onComplete(new Result.Success<File>(localFile));
                } else {
                    callback.onComplete(new Result.Error(task.getException()));
                }
            }
        });
    }

    public void loadAnswer(DataSourceCallback<Result> callback) {
        db.collection("answer")
                .document("answer")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            callback.onComplete(new Result.Success<DocumentSnapshot>(document));
                        }
                    }
                });
    }

    public void loadQuestion(DataSourceCallback<Result> callback) {
        StorageReference ref = firebaseStorage.getReference().child("problemImages");

        ref.getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        callback.onComplete(new Result.Success<Task>(task));
                    }
                });
    }
}
