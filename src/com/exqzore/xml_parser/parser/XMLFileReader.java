package com.exqzore.xml_parser.parser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class XMLFileReader implements Closeable {
    private final String filePath;
    private final BufferedReader br;
    private int COMPLETE_LINES = 5;

    public XMLFileReader(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(this.filePath), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage()); //TODO change to logger
            throw new FileNotFoundException();
        }
    }

    public String readNextBlock() throws IOException {
        StringBuilder sb = new StringBuilder();
        String tmpString;
        int counter = 1;

        try {

            //---------------------------- read 5 lines -----------------------------//
            while ((tmpString = br.readLine()) != null && counter++ < COMPLETE_LINES) {
                sb.append(tmpString);
            }
            if (tmpString != null) {
                sb.append(tmpString);
            }
            //-----------------------------------------------------------------------//


            //------------------------ read to the symbol '>' -----------------------//
            if (sb.toString().length() > 0) {
                if (sb.toString().charAt(sb.toString().length() - 1) != '>') {
                    int nextByte;
                    char symbol = '\0';
                    while ((nextByte = br.read()) != -1 && (symbol = (char) nextByte) != '>') {
                        sb.append(symbol);
                    }
                    if (symbol != '\0')
                        sb.append(symbol);
                }
            }
            //-----------------------------------------------------------------------//

        }catch (IOException e) {
            System.out.println(e.getMessage()); //TODO change to logger
            throw new IOException();
        }

        return sb.toString();
    }

    @Override
    public void close() throws IOException {
        br.close();
    }
}
