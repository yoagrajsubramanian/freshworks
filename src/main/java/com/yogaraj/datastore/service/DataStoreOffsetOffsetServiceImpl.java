package com.yogaraj.datastore.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * {@link DataStoreOffsetOffsetServiceImpl} holds the offset position of each key in the data store
 *
 * @author yogaraj
 */
@Service
public class DataStoreOffsetOffsetServiceImpl implements DataStoreOffsetService {

    private HashMap<String, String> keyPositionMapper = new HashMap<>();

    @Override
    public void insertKeyOffset(String key, Long position, Long offset) {
        this.keyPositionMapper.put(key, String.format("%d;%d", position, offset));
    }

    @Override
    public void deleteKeyOffset(String key) {
        this.keyPositionMapper.remove(key);
    }

    @Override
    public String getKeyOffset(String key) {
        return this.keyPositionMapper.get(key);
    }
}

