package com.apna_store.common_service.messages;

public interface ExceptionMessages {

    String ERROR_READING_FILE = "Error reading file bytes";

    String ERROR_UPLOADING_FILE_TO_S3 = "Error uploading file to S3";

    String ERROR_DOWNLOADING_FILE = "Error downloading file from S3";

    String KEY_ALREADY_EXIST = "Data Already Exist With This Key";

    String ERROR_DELETING_FILE = "Error deleting file from S3";
    String DATA_NOT_FOUND = "Data Not Found";
}
