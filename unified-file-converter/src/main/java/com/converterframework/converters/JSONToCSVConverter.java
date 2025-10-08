package com.converterframework.converters;

import com.converterframework.interfaces.FileConverter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Converter for JSON to CSV format.
 * Converts a JSON array of objects into a CSV file.
 */
public class JSONToCSVConverter implements FileConverter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supportsFormat(String from, String to) {
        return "JSON".equals(from) && "CSV".equals(to);
    }

    @Override
    public void convert(File inputFile, File outputFile) throws IOException {
        if (!inputFile.exists() || !inputFile.canRead()) {
            throw new IllegalArgumentException("Input file does not exist or cannot be read: " + inputFile.getPath());
        }

        JsonNode rootNode = objectMapper.readTree(inputFile);

        if (!rootNode.isArray()) {
            throw new IllegalArgumentException("Input JSON must be an array of objects.");
        }

        ArrayNode jsonArray = (ArrayNode) rootNode;

        if (jsonArray.isEmpty()) {
            // Create an empty file
            outputFile.createNewFile();
            return;
        }

        // Get headers from the first object
        List<String> headers = new ArrayList<>();
        Iterator<String> fieldNames = jsonArray.get(0).fieldNames();
        while (fieldNames.hasNext()) {
            headers.add(fieldNames.next());
        }

        try (PrintWriter writer = new PrintWriter(outputFile)) {
            // Write header
            writer.println(String.join(",", headers));

            // Write data rows
            for (JsonNode node : jsonArray) {
                List<String> row = new ArrayList<>();
                for (String header : headers) {
                    JsonNode valueNode = node.get(header);
                    String value = (valueNode != null && !valueNode.isNull()) ? valueNode.asText() : "";
                    row.add(escapeCsv(value));
                }
                writer.println(String.join(",", row));
            }
        }
    }

    @Override
    public String getConverterName() {
        return "JSON to CSV Converter";
    }

    private String escapeCsv(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
