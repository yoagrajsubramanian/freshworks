package com.yogaraj.datastore.utils;

import com.yogaraj.datastore.constant.DataStoreConstant;
import com.yogaraj.datastore.constant.ErrorConstant;
import com.yogaraj.datastore.exception.FileSizeLimitException;
import com.yogaraj.datastore.exception.InvalidFilePathException;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileUtils {

    public static void checkIfVaildFilePathProvided(@NonNull String filePath) throws InvalidFilePathException {

        if (StringUtils.isEmpty(filePath))
            throw new InvalidFilePathException(String.format(ErrorConstant.ERROR_INVALID_FILE_FORMAT, filePath));

        File file = new File(filePath);
        if (!file.isAbsolute())
            throw new InvalidFilePathException(String.format(ErrorConstant.ERROR_INVALID_FILE_FORMAT, filePath));
    }

    public static void createRootFolder(String dirPath) {

        File file = new File(dirPath);
        if (!file.exists()) file.mkdirs();
    }

    public static void createDataSourceFile(String filePath) throws IOException {
        File file = new File(filePath);
        //delete old data.
        if (file.exists())
            file.delete();

        file.createNewFile();
    }

    public static String getDataSourcePath() {
        return DataStoreConstant.DATASOURCE;
    }

    public static void checkDataStoreFileSize() throws FileNotFoundException {
        File file = new File(getDataSourcePath());

        if (!file.exists())
            throw new FileNotFoundException();

        if (file.length() > 1e+9){
            throw new FileSizeLimitException(ErrorConstant.ERROR_DATA_STORELIMIT_REACHED);
        }
    }
}
