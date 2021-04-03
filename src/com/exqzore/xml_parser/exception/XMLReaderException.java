package com.exqzore.xml_parser.exception;

public class XMLReaderException extends Exception {
    public XMLReaderException() {
        super();
    }

    public XMLReaderException(String message) {
        super(message);
    }

    public XMLReaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public XMLReaderException(Throwable cause) {
        super(cause);
    }
}
