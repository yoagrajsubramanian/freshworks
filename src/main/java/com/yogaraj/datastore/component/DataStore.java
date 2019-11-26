package com.yogaraj.datastore.component;

import com.yogaraj.datastore.constant.DataStoreConstant;
import com.yogaraj.datastore.exception.InvalidFilePathException;
import com.yogaraj.datastore.exception.InvalidRequestException;
import com.yogaraj.datastore.exception.KeyExpiredException;
import com.yogaraj.datastore.service.LoggerService;
import com.yogaraj.datastore.storage.KeyStorePersistence;
import com.yogaraj.datastore.utils.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * {@link DataStore} a key-value store provider where {@link JSONObject} are stored against
 * a {@link String}.
 * <p>
 * {@link DataStore} if the bean is created with out filepath it takes the default file location
 * {@link DataStoreConstant#DEFAULT_DIR} and file name {@link DataStoreConstant#DEFAULT_FILE}
 * <p>
 * To create {@link DataStore} bean with file path follow up the example below
 * --------------------------------------------------------------------------
 * '@Bean'
 * DataStore dataStore() throws IOException {
 * return new DataStore("home/yogaraj/fw/datastore/soruce.txt");
 * }
 * --------------------------------------------------------------------------
 * <p>
 * If not use {@link DataStore#updateDataStorePath(String)} to update source path at any given instance
 * </p>
 *
 * <p>
 * To apply TTL to object use the key TTL in your payload. if the key is not matched TTL will not be validated
 * </p>
 *
 * @author yogaraj
 */
@Component
public class DataStore {

    @Autowired
    private LoggerService loggerService;

    @Autowired
    private KeyStorePersistence keyStorePersistence;

    @Autowired
    private DataStoreValidation dataStoreValidation;

    private String filePath;

    public DataStore(@Value("") String filePath) throws IOException {

        if (StringUtils.isEmpty(filePath)) {
            this.initDefaultDataStore();
        } else {
            try {
                FileUtils.checkIfVaildFilePathProvided(this.filePath);
                try {
                    FileUtils.createDataSourceFile(this.filePath);
                } catch (IOException e) {
                    throw new IOException(e.getMessage());
                }
                DataStoreConstant.DATASOURCE = this.filePath;
            } catch (InvalidFilePathException e) {
                this.initDefaultDataStore();
            }
        }
    }

    /**
     * @param filePath {@link String}
     * @throws IOException throws IOException if file resource cant be accessed
     */
    public void updateDataStorePath(String filePath) throws IOException {
        FileUtils.checkIfVaildFilePathProvided(this.filePath);
        try {
            FileUtils.createDataSourceFile(this.filePath);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
        DataStoreConstant.DATASOURCE = this.filePath;
    }

    /**
     * creates default file location {@link DataStoreConstant#DEFAULT_DIR} under current working dir
     *
     * @throws IOException throws IOException if file resource cant be accessed
     */
    private void initDefaultDataStore() throws IOException {

        String currentWorkingDirectory = System.getProperty("user.dir");

        FileUtils.createRootFolder(String.format("%s%s", currentWorkingDirectory,
                DataStoreConstant.DEFAULT_DIR));

        this.filePath = String.format("%s%s%s.txt", currentWorkingDirectory,
                DataStoreConstant.DEFAULT_DIR, DataStoreConstant.DEFAULT_FILE);

        try {
            FileUtils.createDataSourceFile(this.filePath);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }

        DataStoreConstant.DATASOURCE = this.filePath;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onDataStoreReady() {
        loggerService.logInfo(DataStore.class.getSimpleName(), "Data Store is Ready to use");
        loggerService.logInfo(DataStore.class.getSimpleName(), "Current Store Location " + DataStoreConstant.DATASOURCE);
    }

    /**
     * Insert Data in the data source file
     * Refer {@link DataStoreValidation} for key value constraints
     *
     * @param key     {@link String}
     * @param payload {@link JSONObject}
     * @throws InvalidRequestException throws if the request is invalid
     * @throws IOException             throws if the file source cant be accessed
     */
    public void put(@NonNull String key, @NonNull JSONObject payload) throws InvalidRequestException, IOException {
        this.dataStoreValidation.validatePutRequest(key, payload, this.keyStorePersistence);
        FileUtils.checkDataStoreFileSize();
        this.keyStorePersistence.putData(key.trim(), payload);
    }

    /**
     * Read data from data source using key.
     *
     * @param key {@link String} the key against which the data is beign stored
     * @return {@link JSONObject}
     * @throws JSONException throws if the data cant be serialized to json object
     * @throws IOException   throws if the data source file cant be accessed
     */
    public JSONObject getData(String key) throws JSONException, IOException {

        JSONObject jsonObject = this.keyStorePersistence.getData(key.trim());

        if (jsonObject.has("TTL")) {
            long ttl = jsonObject.getLong("TTL");
            if (ttl < System.currentTimeMillis()) {
                throw new KeyExpiredException("Request Key Has been Expired");
            }
        }

        return jsonObject;
    }

    public void deleteKey(String key) throws IOException, JSONException {
        //double check if get functionality is working.
        this.getData(key.trim());
        this.keyStorePersistence.deleteData(key.trim());
    }
}
