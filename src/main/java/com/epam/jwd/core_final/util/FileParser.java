package com.epam.jwd.core_final.util;

import com.epam.jwd.core_final.exception.InvalidFileFormatException;

public interface FileParser {
    String FILE_NOT_FOUND_MSG = "File not found";
    String FILE_IS_EMPTY_MSG = "File is empty";
    String INVALID_FILE_FORMAT_MSG = "File has invalid format";

    void fillEntityStorage() throws InvalidFileFormatException;
}
