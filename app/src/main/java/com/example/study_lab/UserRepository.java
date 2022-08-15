package com.example.study_lab;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.study_lab.datasource.DataSource;
import com.example.study_lab.datasource.DataSourceCallback;
import com.example.study_lab.datasource.ListenerCallback;
import com.example.study_lab.model.Result;
import com.example.study_lab.model.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

public class UserRepository {
    private static volatile UserRepository INSTANCE = new UserRepository();

    private DataSource dataSource;
    private FileService fileService;
    protected Executor executor;

    private Map<String, Drawable> userQrDrawableMap = new HashMap<String, Drawable>();
    private Map<String, User> userMap = new HashMap<>();
    private MutableLiveData<Boolean> userCheckedIn = new MutableLiveData<>(false);

    public static UserRepository getInstance() {
        return INSTANCE;
    }

    public void tryRegister(final String id, final String password, final String displayName, final String phoneNum,
                            final String checkIn, UserRepositoryCallback<Result> callback) {
        dataSource.tryRegister(id, password, displayName, phoneNum, checkIn, callback::onComplete);
    }

    public void tryLogin(final String id, final String password, UserRepositoryCallback<Result> callback) {
        dataSource.tryLogin(id, password, callback::onComplete);
    }

    public void getAllUsersId(final UserRepositoryCallback<Result> callback) {
        dataSource.getAllUsersId(callback::onComplete);
    }

    public User getUser(String id) {
        if (userMap.containsKey(id))
            return userMap.get(id);
        return null;
    }

    public void changeCheckInState(String id, UserRepositoryCallback<Result> callback) {
        dataSource.changeCheckInState(id, callback::onComplete);
    }

    public void getAnswer(UserRepositoryCallback<Result> callback) {
        dataSource.getAnswer(callback::onComplete);
    }

    public void loadQuestion(UserRepositoryCallback<Result> callback) {
        fileService.getImageDrawable("problemImages/problem_todayQuestion.jpg", new FileService.FileServiceCallback<Result<Drawable>>() {
            @Override
            public void onComplete(Result result) {
                callback.onComplete(result);
            }
        });
    }

    public void setUser(String id, UserRepositoryCallback<Result> callback) {
        dataSource.getUserInformation(id, new DataSourceCallback<Result>() {
            @Override
            public void onComplete(Result result) {
                if (result instanceof Result.Success) {
                    DocumentSnapshot documentSnapshot = ((Result.Success<DocumentSnapshot>) result).getData();
                    User tmpUser = new User(
                            Objects.requireNonNull(documentSnapshot.get("name")).toString(),
                            Objects.requireNonNull(documentSnapshot.get("id")).toString(),
                            Objects.requireNonNull(documentSnapshot.get("password")).toString(),
                            Objects.requireNonNull(documentSnapshot.get("phoneNumber")).toString(),
                            Objects.requireNonNull(documentSnapshot.get("checkIn")).toString()
                    );
                    userMap.put(id, tmpUser);
                    callback.onComplete(result);
                }
            }
        });
    }

    public void createQrForUser(final User toAdd, UserRepositoryCallback<Result> callback) {
        generateUserQr(toAdd, new UserRepositoryCallback<Result>() {
            @Override
            public void onComplete(Result result) {
                callback.onComplete(result);
            }
        });
    }

    private void generateUserQr(User user, UserRepositoryCallback<Result> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String toEncode = "gausslab.study_lab.user_" + user.getUserId();
                generateUserQr_helper(toEncode, App.getQrImagePath(user.getUserId()), new UserRepositoryCallback<Result>() {
                    @Override
                    public void onComplete(Result result) {
                        callback.onComplete(result);
                    }
                });
            }
        });
    }

    private void generateUserQr_helper(String toEncode, String localDestinationPath, UserRepositoryCallback<Result> callback) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(toEncode, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            fileService.saveBitmapToDisk(localDestinationPath, bmp, new FileService.FileServiceCallback<Result<File>>() {
                @Override
                public void onComplete(Result<File> result) {
                    if (result instanceof Result.Success) {
                        File localFile = ((Result.Success<File>) result).getData();
                        fileService.uploadFileToDatabase(localFile, localDestinationPath, new FileService.FileServiceCallback<Result<Uri>>() {
                            @Override
                            public void onComplete(Result<Uri> result) {
                                callback.onComplete(result);
                            }
                        });
                    } else {
                        callback.onComplete(new Result.Error(new Exception("DeviceRepository : createQr() : Problem saving QR bitmap to disk")));
                    }
                }
            });
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void loadQrDrawableForUser(String id, UserRepositoryCallback<Result<Drawable>> callback) {
        fileService.getImageDrawable(App.getQrImagePath(id), new FileService.FileServiceCallback<Result<Drawable>>() {
            @Override
            public void onComplete(Result result) {
                if (result instanceof Result.Success) {
                    Drawable drawable = ((Result.Success<Drawable>) result).getData();
                    userQrDrawableMap.put(id, drawable);
                }
                callback.onComplete(result);
            }
        });
    }

    public void getUserCheckInState(String id) {
        dataSource.getUserCheckInState(id, new ListenerCallback<Result<String>>() {
            @Override
            public void onUpdate(Result<String> result) {
                if (result instanceof Result.Success) {
                    String isCheckIn = ((Result.Success<String>) result).getData();
                    if (isCheckIn.equals("true")) {
                        userCheckedIn.postValue(true);
                    }
                }
            }
        });
    }

    public LiveData<Boolean> isUserCheckedIn() {
        return userCheckedIn;
    }

    public Drawable getQrDrawable(String id) {
        return userQrDrawableMap.get(id);
    }

    public void setExecutor(Executor exec) {
        this.executor = exec;
    }

    public void setFileService(FileService fs) {
        this.fileService = fs;
    }

    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
    }

    public interface UserRepositoryCallback<Result> {
        void onComplete(Result result);
    }
}
