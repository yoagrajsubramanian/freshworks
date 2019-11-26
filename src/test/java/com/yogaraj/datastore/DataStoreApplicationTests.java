package com.yogaraj.datastore;

import com.yogaraj.datastore.component.DataStore;
import com.yogaraj.datastore.exception.InvalidRequestException;
import com.yogaraj.datastore.exception.KeyExpiredException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.io.IOException;


@SpringBootTest
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class DataStoreApplicationTests {

    @Autowired
    private DataStore dataStore;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("Check If Data Store Object Is Initialized")
    void dataStoreObjectTest() {
        Assert.notNull(dataStore, DataStore.class.getSimpleName() + " isn't Initialized");
    }

    @DisplayName("Insert Invalid Key - empty string")
    @Test
    void insertInvalidKey() {
        try {
            dataStore.put("", new JSONObject());
            Assertions.fail("System Accepts empty key");
        } catch (InvalidRequestException | IOException e) {
            Assert.isTrue(true, e.getMessage());
        }
    }

    @DisplayName("Insert Invalid Key - length greater than 16")
    @Test
    void insertInvalidKeyLength() {
        try {
            dataStore.put("ksdkbskdsdfbskdfsdfskdfsdfsdf", new JSONObject());
            Assertions.fail("System Accepts Invalid Key Length");
        } catch (InvalidRequestException e) {
            Assert.isTrue(true, e.getMessage());
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @DisplayName("Insert Invalid Payload - null ")
    @Test
    void insertInvalidPayload() {
        try {
            dataStore.put("test-key 1 ", null);
            Assertions.fail("System Accepts Invalid Payload");
        } catch (InvalidRequestException e) {
            Assert.isTrue(true, e.getMessage());
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @DisplayName("Insert valid Payload - length < 16KB & Key < 16 Char ")
    @Test
    void insertValidPayload() {
        try {
            JSONObject jsonObject = new JSONObject();
            //adding a buffer of 6sec to check ttl
            jsonObject.put("TTL", System.currentTimeMillis() + 6000);
            dataStore.put("test-keyw/ottl", jsonObject);

            jsonObject = new JSONObject();
            jsonObject.put("TTL", System.currentTimeMillis() );
            dataStore.put("test-keywttl", jsonObject);

        } catch (InvalidRequestException | IOException | JSONException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @DisplayName("Read & Delete Data")
    @Test
    void readData() {
        try {
            JSONObject jsonObject = dataStore.getData("test-keyw/ottl");
            if (jsonObject == null)
                Assert.isNull(jsonObject, "UNABLE TO READ DATA");

            dataStore.deleteKey("test-keyw/ottl");
        } catch (InvalidRequestException | IOException | JSONException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @DisplayName("Read / Delete Expired Data")
    @Test
    void readExpiredData() {
        try {
            JSONObject jsonObject = dataStore.getData("test-keywttl");

            if (jsonObject != null)
                Assertions.fail("System is reading expired data");

        } catch (KeyExpiredException e) {
            Assert.isTrue(true, e.getMessage());
        } catch (InvalidRequestException | IOException | JSONException e) {
            Assertions.fail(e.getMessage());
        }
    }

}
