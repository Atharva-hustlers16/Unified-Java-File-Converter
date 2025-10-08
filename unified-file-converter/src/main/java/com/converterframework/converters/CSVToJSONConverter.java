package com.converterframework.converters;

import com.converterframework.interfaces.FileConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Converter for CSV to JSON format.
 */
public class CSVToJSONConverter implements FileConverter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supportsFormat(String from, String to) {
        return "CSV".equals(from) && "JSON".equals(to);
    }

    @Override
    public void convert(File inputFile, File outputFile) throws Exception {
        if (!inputFile.exists() || !inputFile.canRead()) {
            throw new IllegalArgumentException("Input file does not exist or cannot be read: " + inputFile.getPath());
        }

        List<String> lines = Files.readAllLines(inputFile.toPath());
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Input file is empty");
        }

        // Parse CSV
        List<String[]> csvData = parseCSV(lines);
        if (csvData.isEmpty()) {
            throw new IllegalArgumentException("No valid data found in CSV file");
        }

        // Convert to JSON
        ArrayNode jsonArray = objectMapper.createArrayNode();

        if (csvData.size() > 1) { // Has header
            String[] headers = csvData.get(0);
            for (int i = 1; i < csvData.size(); i++) {
                String[] values = csvData.get(i);
                ObjectNode jsonObject = objectMapper.createObjectNode();

                for (int j = 0; j < headers.length && j < values.length; j++) {
                    jsonObject.put(headers[j], values[j]);
                }

                jsonArray.add(jsonObject);
            }
        } else { // No header, treat as single row
            String[] values = csvData.get(0);
            ObjectNode jsonObject = objectMapper.createObjectNode();

            for (int i = 0; i < values.length; i++) {
                jsonObject.put("field" + (i + 1), values[i]);
            }

            jsonArray.add(jsonObject);
        }

        // Write JSON
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, jsonArray);
    }

    @Override
    public String getConverterName() {
        return "CSV to JSON Converter";
    }

    /**
     * Parses CSV lines into a list of string arrays.
     */
    private List<String[]> parseCSV(List<String> lines) {
        return lines.stream()
            .filter(line -> !line.trim().isEmpty())
            .map(this::parseCSVLine)
            .filter(row -> row.length > 0)
            .toList();
    }

    /**
     * Parses a single CSV line into an array of values.
     */
    private String[] parseCSVLine(String line) {
        // Simple CSV parser - handles quoted values and commas
        if (line == null || line.trim().isEmpty()) {
            return new String[0];
        }

        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // Escaped quote
                    current.append('"');
                    i++; // Skip next quote
                } else {
                    // Toggle quote state
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // Field separator
                result.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        result.add(current.toString().trim());
        return result.toArray(new String[0]);
    }
}
