package com.exqzore.xml_parser.exception;

public class XMLParserException extends Exception{
    public XMLParserException() {
        super();
    }

    public XMLParserException(String message) {
        super(message);
    }

    public XMLParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public XMLParserException(Throwable cause) {
        super(cause);
    }
}
