package com.yogaraj.datastore.storage;

import com.yogaraj.datastore.constant.DataStoreConstant;
import com.yogaraj.datastore.constant.ErrorConstant;
import com.yogaraj.datastore.exception.InvalidRequestException;
import com.yogaraj.datastore.exception.KeyExpiredException;
import com.yogaraj.datastore.service.DataStoreOffsetService;
import com.yogaraj.datastore.service.LoggerService;
import com.yogaraj.datastore.utils.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Component
public class KeyStorePersistence {

    @Autowired
    LoggerService loggerService;

    @Autowired
    DataStoreOffsetService dataStoreOffsetService;

    public void putData(String key, JSONObject jsonObject) throws IOException {
        File file = new File(FileUtils.getDataSourcePath());
        if (!file.exists()) {
            throw new NullPointerException("File not found exception");
        }

        FileOutputStream fileOutputStream = new FileOutputStream(file, true);

        FileChannel fileChannel = fileOutputStream.getChannel();

        try {
            fileChannel.tryLock();
        }catch (IOException e){
            throw new IOException("Resource Already Inuse Exception");
        }

        String content = String.format("%s%s&%s%s", DataStoreConstant.DELIMITER_START,
                key, jsonObject.toString(), DataStoreConstant.DELIMITER_END);

        ByteBuffer byteBuffer = ByteBuffer.wrap(content.getBytes());

        fileChannel.write(byteBuffer, fileChannel.position());

        dataStoreOffsetService.insertKeyOffset(key, fileChannel.position(), (long) byteBuffer.limit());

        fileChannel.close();
    }

    public JSONObject getData(String key) throws IOException, JSONException {

        String dataPositionOffset = this.dataStoreOffsetService.getKeyOffset(key);

        if (StringUtils.isEmpty(dataPositionOffset)){
            return null;
        }

        long position = Long.parseLong(dataPositionOffset.split(";")[0]);

        long offset = Long.parseLong(dataPositionOffset.split(";")[1]);

        File file = new File(FileUtils.getDataSourcePath());

        if (!file.exists()) {
            throw new NullPointerException("File not found exception");
        }

        FileInputStream inputStream = new FileInputStream(file);

        FileChannel channel = inputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate((int) offset);

        channel.read(byteBuffer, position - offset);

        channel.close();

        String response = new String(byteBuffer.array());

        response = response.substring(response.indexOf(DataStoreConstant.DELIMITER_START),
                response.indexOf(DataStoreConstant.DELIMITER_END));

        int keyIndex = response.indexOf("&");

        return new JSONObject(response.substring(keyIndex + 1));
    }

    public void deleteData(String key) throws IOException, JSONException {

        String dataPositionOffset = this.dataStoreOffsetService.getKeyOffset(key);

        if (StringUtils.isEmpty(dataPositionOffset)){
            throw new InvalidRequestException(ErrorConstant.ERROR_INVALID_KEY);
        }

        long position = Long.parseLong(dataPositionOffset.split(";")[0]);

        long offset = Long.parseLong(dataPositionOffset.split(";")[1]);

        File file = new File(FileUtils.getDataSourcePath());

        if (!file.exists()) {
            throw new NullPointerException(ErrorConstant.ERROR_FILE_NOT_FOUND);
        }

        FileOutputStream fileOutputStream = new FileOutputStream(file, true);

        FileChannel fileChannel = fileOutputStream.getChannel();

        try {
            fileChannel.tryLock();
        }catch (IOException e){
            throw new IOException("Resource Already Inuse Exception");
        }

        byte[] emptyByte = new byte[(int)offset];

        for (int i = 0; i < (int) offset; i++) {
            //here 127 is the ascii value for delete
            emptyByte[i]=127;
        }

        ByteBuffer byteBuffer = ByteBuffer.wrap(emptyByte);

        fileChannel.write(byteBuffer,position - offset);

        fileChannel.close();

        dataStoreOffsetService.deleteKeyOffset(key);
    }

    public boolean checkIfKeyPresent(String key){
        String dataPositionOffset = this.dataStoreOffsetService.getKeyOffset(key);
        return StringUtils.isEmpty(dataPositionOffset);
    }

}
