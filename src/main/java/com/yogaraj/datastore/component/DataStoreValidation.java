package com.yogaraj.datastore.component;

import com.yogaraj.datastore.constant.DataStoreConstant;
import com.yogaraj.datastore.constant.ErrorConstant;
import com.yogaraj.datastore.exception.InvalidRequestException;
import com.yogaraj.datastore.exception.KeyAlreadyExistsException;
import com.yogaraj.datastore.storage.KeyStorePersistence;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * {@link DataStoreValidation} handles the validation rules before inserting into the data store
 *
 * @author yogaraj
 */
@Component
public class DataStoreValidation {

    void validatePutRequest(String key, JSONObject jsonObject, KeyStorePersistence keyStorePersistence)
            throws InvalidRequestException {

        if (StringUtils.isEmpty(key)) {
            throw new InvalidRequestException(ErrorConstant.ERROR_INVALID_KEY);
        }

        if (key.length() > DataStoreConstant.MAX_KEY_LENGTH) {
            throw new InvalidRequestException(ErrorConstant.ERROR_INVALID_KEY_LENGTH);
        }

        if (jsonObject == null) {
            throw new InvalidRequestException(ErrorConstant.ERROR_INVALID_PAYLOAD);
        }
        byte[] payloadBytes = jsonObject.toString().getBytes();

        if (payloadBytes.length > DataStoreConstant.MAX_PAYLOAD_LENGTH) {
            throw new InvalidRequestException(ErrorConstant.ERROR_MAX_PAYLOAD_REACHED);
        }

        if (!keyStorePersistence.checkIfKeyPresent(key)) {
            throw new KeyAlreadyExistsException(ErrorConstant.ERROR_KEY_ALREADY_EXISTS);
        }
    }
}
