package com.exqzore.xml_parser.parser.reader;

import com.exqzore.xml_parser.exception.XMLReaderException;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class XMLFileReader implements Closeable {
    private static final char LAST_CHAR_OF_BLOCK = '>';
    private static final char TERMINAL_ZERO = '\0';
    private static final int COMPLETE_LINES = 5;
    private final BufferedReader bufferedReader;
    private String path;

    public XMLFileReader(String filePath) throws XMLReaderException {
        path = filePath;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
        } catch (FileNotFoundException exception) {
            System.out.format("File \"%s\" was not found, error message is: %s", path, exception.getMessage()); //TODO change to logger
            throw new XMLReaderException(exception);
        }
    }

    public String readNextBlock() throws XMLReaderException {
        String block;
        try {
            block = readCompleteLines();
            block = readToLastChar(block);
        }catch (XMLReaderException exception) {
            System.out.println(exception.getMessage()); //TODO change to logger
            throw new XMLReaderException(exception);
        }
        return block;
    }

    private String readToLastChar(String data) throws XMLReaderException {
        String result = data;
        try {
            if (data != null && data.length() > 0) {
                if (data.charAt(data.length() - 1) != LAST_CHAR_OF_BLOCK) {
                    StringBuilder sb = new StringBuilder(data);
                    int nextByte;
                    char symbol = TERMINAL_ZERO;
                    while ((nextByte = bufferedReader.read()) != -1 && (symbol = (char) nextByte) != LAST_CHAR_OF_BLOCK) {
                        sb.append(symbol);
                    }
                    if (symbol != TERMINAL_ZERO) {
                        sb.append(symbol);
                    }
                    result = sb.toString();
                }
            }
        }catch (IOException exception) {
            System.out.println(exception.getMessage()); //TODO change to logger
            throw new XMLReaderException(exception);
        }
        return result;
    }

    private String readCompleteLines() throws XMLReaderException {
        StringBuilder sb = new StringBuilder();
        String tmpString;
        int countLines = 1;
        try {
            while ((tmpString = bufferedReader.readLine()) != null && countLines++ < COMPLETE_LINES) {
                sb.append(tmpString);
            }
            if (tmpString != null) {
                sb.append(tmpString);
            }
        }catch (IOException exception) {
            System.out.println(exception.getMessage()); //TODO change to logger
            throw new XMLReaderException(exception);
        }
        return sb.toString();
    }

    @Override
    public void close() throws IOException {
        bufferedReader.close();
    }
}
