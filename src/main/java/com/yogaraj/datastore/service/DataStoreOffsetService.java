package com.yogaraj.datastore.service;

public interface DataStoreOffsetService {
    void insertKeyOffset(String key, Long position,Long offset);

    boolean checkIfKeyAlreadyPresent(String key);

    void deleteKeyOffset(String key);

    String getKeyOffset(String key);
}
