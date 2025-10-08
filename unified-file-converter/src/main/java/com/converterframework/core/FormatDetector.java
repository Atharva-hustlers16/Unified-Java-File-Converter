package com.converterframework.core;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for detecting file formats based on extension and content.
 */
public class FormatDetector {

    private static final Map<String, String> EXTENSION_TO_FORMAT = new HashMap<>();

    static {
        // Initialize extension to format mapping
        EXTENSION_TO_FORMAT.put("csv", "CSV");
        EXTENSION_TO_FORMAT.put("json", "JSON");
        EXTENSION_TO_FORMAT.put("xml", "XML");
        EXTENSION_TO_FORMAT.put("xlsx", "EXCEL");
        EXTENSION_TO_FORMAT.put("xls", "EXCEL");
        EXTENSION_TO_FORMAT.put("txt", "TEXT");
        EXTENSION_TO_FORMAT.put("pdf", "PDF");
    }

    /**
     * Detects the format of a file based on its extension and content.
     *
     * @param file the file to analyze
     * @return the detected format, or "UNKNOWN" if not detected
     */
    public static String detectFormat(File file) {
        if (file == null || !file.exists()) {
            return "UNKNOWN";
        }

        String extension = getFileExtension(file.getName()).toLowerCase();
        String format = EXTENSION_TO_FORMAT.get(extension);

        if (format != null) {
            // For some formats, do a quick content check
            if ("CSV".equals(format)) {
                return isCSVFile(file) ? "CSV" : "UNKNOWN";
            } else if ("JSON".equals(format)) {
                return isJSONFile(file) ? "JSON" : "UNKNOWN";
            } else if ("XML".equals(format)) {
                return isXMLFile(file) ? "XML" : "UNKNOWN";
            } else if ("EXCEL".equals(format)) {
                return isExcelFile(file) ? "EXCEL" : "UNKNOWN";
            } else if ("TEXT".equals(format)) {
                return isTextFile(file) ? "TEXT" : "UNKNOWN";
            }
        }

        return format != null ? format : "UNKNOWN";
    }

    /**
     * Gets the file extension from a filename.
     *
     * @param filename the filename
     * @return the file extension (without the dot)
     */
    private static String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0 ? filename.substring(lastDotIndex + 1) : "";
    }

    /**
     * Checks if a file is a valid CSV file by reading the first few lines.
     */
    private static boolean isCSVFile(File file) {
        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String firstLine = reader.readLine();
            if (firstLine == null) return false;

            // Check if line contains comma-separated values
            return firstLine.contains(",") || firstLine.contains(";") || firstLine.contains("\t");
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Checks if a file is a valid JSON file.
     */
    private static boolean isJSONFile(File file) {
        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String firstLine = reader.readLine();
            if (firstLine == null) return false;

            // Check if line starts with '{' or '['
            return firstLine.trim().startsWith("{") || firstLine.trim().startsWith("[");
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Checks if a file is a valid XML file.
     */
    private static boolean isXMLFile(File file) {
        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String firstLine = reader.readLine();
            if (firstLine == null) return false;

            // Check if line starts with XML declaration or root element
            return firstLine.trim().startsWith("<?xml") || firstLine.trim().startsWith("<");
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Checks if a file is a valid Excel file by checking the magic bytes.
     */
    private static boolean isExcelFile(File file) {
        try {
            byte[] header = Files.readAllBytes(file.toPath());
            if (header.length < 8) return false;

            // Check for Excel file signatures
            // XLSX files start with "PK" (ZIP format)
            if (header[0] == 'P' && header[1] == 'K') {
                return true;
            }

            // XLS files have different signatures, but for simplicity we'll just check extension
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Checks if a file is a text file.
     */
    private static boolean isTextFile(File file) {
        try {
            // Try to read as UTF-8, if it fails, it's probably not a text file
            Files.newBufferedReader(file.toPath());
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
