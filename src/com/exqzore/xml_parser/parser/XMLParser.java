package com.exqzore.xml_parser.parser;

import com.exqzore.xml_parser.entity.Attribute;
import com.exqzore.xml_parser.entity.Node;
import com.exqzore.xml_parser.exception.XMLParserException;
import com.exqzore.xml_parser.exception.XMLReaderException;
import com.exqzore.xml_parser.parser.reader.XMLFileReader;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class XMLParser {
    private final String NAME_OF_NODE = "[^<\\s\\t][^\\s\\t>]+";
    private final String ATTRIBUTES_OF_NODE = "\\S+=\"[^\"]*\"";
    private final String NAME_VALUE_OF_ATTRIBUTE = "[^\"=]+";

    private final List<Node> nodes = new LinkedList<>();
    private final String filePath;

    public XMLParser(String filePath) {
        this.filePath = filePath;
    }

    public Node parse() throws XMLParserException {
        XMLFileReader xmlFileReader;
        String blockOfLines;
        try {
            xmlFileReader = new XMLFileReader(filePath);
        } catch (XMLReaderException exception) {
            System.out.println(exception.getMessage()); //TODO change to logger
            throw new XMLParserException(exception);
        }
        try (xmlFileReader) {
            while ((blockOfLines = xmlFileReader.readNextBlock()).length() > 0) {
                List<String> lines = blockToListOfTags(blockOfLines);
                parseLines(lines);
            }
        } catch (IOException | XMLReaderException exception) {
            System.out.println(exception.getMessage()); //TODO change to logger
            throw new XMLParserException(exception);
        }
        return getRoot();
    }

    private List<String> blockToListOfTags(String blockOfLines) {
        blockOfLines = blockOfLines.replace("\n", "")
                .replace("\r", "")
                .replace("<", "\n<")
                .replace(">", ">\n");
        List<String> lines = blockOfLines.lines().collect(Collectors.toList());
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).isBlank()) {
                lines.remove(i);
            }
        }
        return lines;
    }

    private Node getRoot() {
        Node node = null;
        if (nodes.size() == 1) {
            node = nodes.get(0);
            nodes.remove(node);
        }
        return node;
    }

    private void parseLines(List<String> lines) {
        for (String str : lines) {
            if (!str.contains("<?")) {
                if (str.contains("</")) {
                    if (nodes.size() > 1) {
                        Node tempNode = nodes.get(nodes.size() - 1);
                        nodes.remove(tempNode);
                        nodes.get(nodes.size() - 1).addNode(tempNode);
                    }
                } else if (str.contains("<")) {
                    Node node = createNode(str);
                    nodes.add(node);
                } else {
                    nodes.get(nodes.size() - 1).setContent(str);
                }
            }
        }
    }

    private Node createNode(String str) {
        Node node = new Node();

        Pattern pattern = Pattern.compile(NAME_OF_NODE);
        Matcher matcher = pattern.matcher(str);
        matcher.find();
        String name = matcher.group();
        node.setName(name);

        List<String> attrs = new ArrayList<>();
        pattern = Pattern.compile(ATTRIBUTES_OF_NODE);
        matcher = pattern.matcher(str);
        while (matcher.find()) {
            attrs.add(matcher.group());
        }
        List<Attribute> attributes = createAttributes(attrs);
        node.setAttributes(attributes);

        return node;
    }

    private List<Attribute> createAttributes(List<String> lines) {
        List<Attribute> attributes = new ArrayList<>();

        for (String str : lines) {
            Pattern pattern = Pattern.compile(NAME_VALUE_OF_ATTRIBUTE);
            Matcher matcher = pattern.matcher(str);
            matcher.find();
            String name = matcher.group();

            matcher.find();
            String value = matcher.group();

            attributes.add(new Attribute(name, value));
        }
        return attributes;
    }
}
