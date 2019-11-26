package com.yogaraj.datastore.service;

public interface DataStoreOffsetService {

    void insertKeyOffset(String key, Long position,Long offset);

    void deleteKeyOffset(String key);

    String getKeyOffset(String key);
}
