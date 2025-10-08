package com.converterframework.core;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for logging conversion operations to a CSV file.
 */
public class LoggerService {

    private static final String LOG_FILE = "conversion_log.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Logs a conversion operation.
     *
     * @param inputFile the input file path
     * @param outputFile the output file path
     * @param fromFormat the source format
     * @param toFormat the target format
     * @param status the conversion status (SUCCESS, FAILED)
     * @param errorMessage optional error message if failed
     */
    public static void logConversion(String inputFile, String outputFile, String fromFormat,
                                   String toFormat, String status, String errorMessage) {
        try {
            // Ensure log directory exists
            Path logPath = Paths.get(LOG_FILE);
            if (!Files.exists(logPath)) {
                Files.createFile(logPath);
                // Write header if new file
                writeLogEntry("Date,Input File,Output File,From Format,To Format,Status,Error Message");
            }

            String logEntry = String.format("%s,%s,%s,%s,%s,%s,%s",
                LocalDateTime.now().format(FORMATTER),
                escapeCSV(inputFile),
                escapeCSV(outputFile),
                fromFormat,
                toFormat,
                status,
                escapeCSV(errorMessage != null ? errorMessage : "")
            );

            writeLogEntry(logEntry);
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }

    /**
     * Logs a successful conversion.
     */
    public static void logSuccess(String inputFile, String outputFile, String fromFormat, String toFormat) {
        logConversion(inputFile, outputFile, fromFormat, toFormat, "SUCCESS", null);
    }

    /**
     * Logs a failed conversion.
     */
    public static void logFailure(String inputFile, String outputFile, String fromFormat,
                                String toFormat, String errorMessage) {
        logConversion(inputFile, outputFile, fromFormat, toFormat, "FAILED", errorMessage);
    }

    /**
     * Reads recent log entries.
     *
     * @param limit maximum number of entries to return
     * @return list of log entries
     */
    public static List<String[]> getRecentLogs(int limit) {
        List<String[]> logs = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(LOG_FILE))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header
                }

                String[] parts = parseCSVLine(line);
                if (parts.length >= 6) {
                    logs.add(parts);
                    if (logs.size() >= limit) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            // File doesn't exist or can't be read, return empty list
        }

        return logs;
    }

    /**
     * Escapes special characters in CSV values.
     */
    private static String escapeCSV(String value) {
        if (value == null) return "";

        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    /**
     * Parses a CSV line into an array of values.
     */
    private static String[] parseCSVLine(String line) {
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
                result.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        result.add(current.toString());
        return result.toArray(new String[0]);
    }

    /**
     * Writes a line to the log file.
     */
    private static void writeLogEntry(String entry) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            writer.println(entry);
        }
    }
}
