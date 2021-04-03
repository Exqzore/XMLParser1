package com.exqzore.xml_parser.main;

import com.exqzore.xml_parser.entity.Node;
import com.exqzore.xml_parser.exception.XMLParserException;
import com.exqzore.xml_parser.parser.XMLParser;

public class Main {
    private static final String PATH_TO_XML = "resources/students2.xml";

    public static void main(String[] args){
        XMLParser xmlParser = new XMLParser(PATH_TO_XML);
        Node root;
        try {
            root = xmlParser.parse();
            System.out.println(root);
        } catch (XMLParserException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
