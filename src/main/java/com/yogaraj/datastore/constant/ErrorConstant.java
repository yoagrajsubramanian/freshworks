package com.yogaraj.datastore.constant;

public class ErrorConstant {

    public static final String ERROR_FILE_NOT_FOUND = "Requested file %s not found";

    public static final String ERROR_INVALID_FILE_FORMAT = "Invalid file format %s provided; " +
            "So default file location will be used.";

    public static final String ERROR_INVALID_KEY = "Invalid Key Provided";

    public static final String ERROR_INVALID_KEY_LENGTH = String.format("Invalid Key Provided : 'Max length %d chars'",
            DataStoreConstant.MAX_KEY_LENGTH);

    public static final String ERROR_MAX_PAYLOAD_REACHED = String.format("Payload size need be reduced : 'Max size %d chars'",
            DataStoreConstant.MAX_KEY_LENGTH);

    public static final String ERROR_INVALID_PAYLOAD = "Invalid Payload Provided";

    public static final String ERROR_DATA_STORELIMIT_REACHED = "Data Store File Size Limit Reached :  'Max Size 1Gib' ";

    public static final String ERROR_KEY_ALREADY_EXISTS = "Provided Key Already Exists";
}
