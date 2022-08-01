package com.example.study_lab.mnist;

import android.content.Context;
import android.util.Log;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Classifier {
    private static final String LOG_TAG = Classifier.class.getSimpleName();

    private Interpreter interpreter;
    private Interpreter.Options options;
    private ByteBuffer byteBuffer;

    public Classifier(Context context){
        options = new Interpreter.Options();

        try {
            interpreter = new Interpreter(FileUtil.loadMappedFile(context, "mnist.tflite"), options);
        }catch (IOException e){
            Log.e(LOG_TAG, "Classifier: IOException ", e);
        }

        byteBuffer = ByteBuffer.allocate(4*28*28);
        byteBuffer.order(ByteOrder.nativeOrder());
    }
}
