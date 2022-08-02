package com.example.study_lab.mnist;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.fragment.app.Fragment;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Classifier {
    private static final String LOG_TAG = Classifier.class.getSimpleName();

    private Interpreter interpreter;
    private Interpreter.Options options;
    private ByteBuffer byteBuffer;
    private float[][] outputBuffer = new float[1][10];
    private int[] pixels = new int[28*28];

    public Classifier(Activity activity){
        options = new Interpreter.Options();

        try {
            interpreter = new Interpreter(loadModelFile(activity), options);
        }catch (IOException e){
            Log.e(LOG_TAG, "Classifier: IOException "+e);
        }

        byteBuffer = ByteBuffer.allocate(4*28*28);
        byteBuffer.order(ByteOrder.nativeOrder());
    }

    public int classify(Bitmap bitmap){
        convertToByteBuffer(bitmap);
        interpreter.run(byteBuffer, outputBuffer);
        return argmax(outputBuffer[0]);
    }

    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd("mnist.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private void convertToByteBuffer(Bitmap bitmap){
        if (byteBuffer==null){ return; }
        byteBuffer.rewind();

        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        int index =0;
        for (int i =0;i<784;i++){
            if (i==781){
                Log.d("convert", "pixel: "+String.format("pixel : %d", index));
            }
            Log.d("convert", "pixel: "+String.format("pixel : %d", index));
            byteBuffer.putFloat(convertPixel(pixels[index++]));
            Log.d("convert", "convertBitmapToByteBuffer: "+String.format("%d %f  pixel : %d", i, convertPixel(pixels[index++]), index));
        }
//        for (int i : new int[27]){
//            for (int j : new int[27]){
//                Log.d("convert", "pixel: "+String.format("pixel : %d", index));
//                byteBuffer.putFloat(convertPixel(pixels[index++]));
//                Log.d("convert", "convertBitmapToByteBuffer: "+String.format("%d %f  pixel : %d", i, convertPixel(pixels[index++]), index));
//            }
//        }

    }

    private static float convertPixel(int color){
        return (255 - (((color >> 16) & 0xFF) * 0.299f
                + ((color >> 8) & 0xFF) * 0.587f
                + (color & 0xFF) * 0.114f)) / 255.0f;
    }

    private static int argmax(float[] array) {
        int maxIdx = -1;
        float maxProb = 0.0f;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > maxProb) {
                maxProb = array[i];
                maxIdx = i;
            }
        }
        return maxIdx;
    }

}
