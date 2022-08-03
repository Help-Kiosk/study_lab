package com.example.study_lab.mnist;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Classifier {
    private static final String LOG_TAG = Classifier.class.getSimpleName();

    private final Interpreter interpreter;
    private final Interpreter.Options options;
    private final ByteBuffer byteBuffer;
    private final float[][] outputBuffer = new float[1][10];
    private final int[] pixels = new int[28*28];

    public Classifier(Activity activity) throws IOException {
        options = new Interpreter.Options();

        interpreter = new Interpreter(loadModelFile(activity), options);
        byteBuffer = ByteBuffer.allocateDirect( 4 * 28 * 28 );
        byteBuffer.order(ByteOrder.nativeOrder());
    }

    public int classify(Bitmap bitmap){
        convertToByteBuffer(bitmap);
        interpreter.run(byteBuffer, outputBuffer);
        return argmax(outputBuffer[0]);
    }

    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd( "mnist.tflite");
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
        for (int i =0;i<28*28;i++){
            byteBuffer.putFloat(convertPixel(pixels[index++]));
        }

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
