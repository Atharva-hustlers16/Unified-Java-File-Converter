package com.converterframework.converters;

import com.converterframework.interfaces.FileConverter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;

/**
 * Converter for JSON to XML format.
 */
public class JSONToXMLConverter implements FileConverter {

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final XmlMapper xmlMapper = new XmlMapper();

    @Override
    public boolean supportsFormat(String from, String to) {
        return "JSON".equals(from) && "XML".equals(to);
    }

    @Override
    public void convert(File inputFile, File outputFile) throws IOException {
        if (!inputFile.exists() || !inputFile.canRead()) {
            throw new IllegalArgumentException("Input file does not exist or cannot be read: " + inputFile.getPath());
        }

        // Read JSON
        JsonNode jsonTree = jsonMapper.readTree(inputFile);

        // Write XML
        xmlMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, jsonTree);
    }

    @Override
    public String getConverterName() {
        return "JSON to XML Converter";
    }
}
