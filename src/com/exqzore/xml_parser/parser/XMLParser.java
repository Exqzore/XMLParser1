package com.exqzore.xml_parser.parser;

import com.exqzore.xml_parser.entity.Attribute;
import com.exqzore.xml_parser.entity.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class XMLParser {
    private final Vector<Node> nodes = new Vector<>();
    private final String filePath;

    public XMLParser(String filePath) {
        this.filePath = filePath;
    }

    public Node parse() throws IOException {
        XMLFileReader xmlFileReader = new XMLFileReader(filePath);
        String blockOfLines;
        try {
            while ((blockOfLines = xmlFileReader.readNextBlock()).length() > 0) {
                List<String> lines = blockToListOfTags(blockOfLines);
                parseLines(lines);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage()); //TODO change to logger
            xmlFileReader.close();
        }
        return getRoot();
    }

    private List<String> blockToListOfTags(String blockOfLines) {
        blockOfLines = blockOfLines.replace("\n", "").replace("\r", "")
                .replace("<", "\n<").replace(">", ">\n");
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
            node = nodes.lastElement();
            nodes.remove(node);
        }
        return node;
    }

    private void parseLines(List<String> lines) {
        for (String str : lines) {
            if (!str.contains("<?")) {
                if (str.contains("</")) {
                    if (nodes.size() > 1) {
                        Node tmp = nodes.lastElement();
                        nodes.remove(tmp);
                        nodes.lastElement().addNode(tmp);
                    }
                } else if (str.contains("<")) {
                    Node node = createNode(str);
                    nodes.add(node);
                } else {
                    nodes.lastElement().setContent(str);
                }
            }
        }
    }

    private Node createNode(String str) {
        Node node = new Node();

        //------------------- name of node ----------------------//
        Pattern pattern = Pattern.compile("[^<\\s\\t][^\\s\\t>]+");
        Matcher matcher = pattern.matcher(str);
        matcher.find();
        String name = matcher.group();

        node.setName(name);
        //-------------------------------------------------------//


        //-------------------- attributes -----------------------//
        List<String> attrs = new ArrayList<>();
        pattern = Pattern.compile("\\S+=\"[^\"]*\"");
        matcher = pattern.matcher(str);
        while (matcher.find()) {
            attrs.add(matcher.group());
        }
        List<Attribute> attributes = createAttributes(attrs);
        node.setAttributes(attributes);
        //-------------------------------------------------------//

        return node;
    }

    private List<Attribute> createAttributes(List<String> lines) {
        List<Attribute> attributes = new ArrayList<>();
        for (String str : lines) {

            //------------------- name of attribute ----------------------//
            Pattern pattern = Pattern.compile("[^\"=]+");
            Matcher matcher = pattern.matcher(str);
            matcher.find();
            String name = matcher.group();
            //------------------------------------------------------------//


            //------------------- value of attribute ---------------------//
            matcher.find();
            String value = matcher.group();
            //------------------------------------------------------------//

            attributes.add(new Attribute(name, value));
        }
        return attributes;
    }
}
