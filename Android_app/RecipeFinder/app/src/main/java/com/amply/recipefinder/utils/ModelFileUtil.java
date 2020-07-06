package com.amply.recipefinder.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

import androidx.annotation.NonNull;

import org.tensorflow.lite.support.common.SupportPreconditions;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class ModelFileUtil {
    private ModelFileUtil() {
    }

    @NonNull
    public static List<String> loadLabels(@NonNull Context context, @NonNull String filePath) throws IOException {
        SupportPreconditions.checkNotNull(context, "Context cannot be null.");
        SupportPreconditions.checkNotNull(filePath, "File path cannot be null.");
        InputStream inputStream = context.getAssets().open(filePath);
        return loadLabels(inputStream);
    }

    @NonNull
    public static List<String> loadLabels(@NonNull InputStream inputStream) throws IOException {
        List<String> labels = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while((line = reader.readLine()) != null) {
            labels.add(line);
        }

        reader.close();
        return labels;
    }

    @NonNull
    public static MappedByteBuffer loadMappedFile(@NonNull Context context, @NonNull String filePath) throws IOException {
        SupportPreconditions.checkNotNull(context, "Context should not be null.");
        SupportPreconditions.checkNotNull(filePath, "File path cannot be null.");
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd(filePath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();


//        File modelFile = new File(filePath);
//        FileInputStream inputStream = new FileInputStream(modelFile);

        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}
