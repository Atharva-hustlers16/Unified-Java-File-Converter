package com.converterframework.core;

import com.converterframework.interfaces.FileConverter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Central manager for file conversions.
 * Handles converter registration and coordinates the conversion process.
 */
public class ConversionManager {

    private final ConcurrentMap<String, FileConverter> converters = new ConcurrentHashMap<>();
    private final List<String> supportedConversions = new ArrayList<>();

    /**
     * Registers a converter for specific format conversions.
     *
     * @param converter the converter to register
     */
    public void registerConverter(FileConverter converter) {
        // Get all supported conversions for this converter
        for (String from : getSupportedFormats()) {
            for (String to : getSupportedFormats()) {
                if (!from.equals(to) && converter.supportsFormat(from, to)) {
                    String key = from + "_TO_" + to;
                    converters.put(key, converter);
                    supportedConversions.add(key);
                }
            }
        }
    }

    /**
     * Performs a file conversion using the appropriate converter.
     *
     * @param inputFile the input file
     * @param outputFile the output file
     * @param fromFormat the source format (can be null to auto-detect)
     * @param toFormat the target format
     * @return true if conversion succeeded, false otherwise
     */
    public boolean convertFile(File inputFile, File outputFile, String fromFormat, String toFormat) {
        if (inputFile == null || outputFile == null || toFormat == null) {
            LoggerService.logFailure(
                inputFile != null ? inputFile.getAbsolutePath() : "null",
                outputFile != null ? outputFile.getAbsolutePath() : "null",
                fromFormat != null ? fromFormat : "UNKNOWN",
                toFormat,
                "Invalid input parameters"
            );
            return false;
        }

        // Auto-detect format if not provided
        String actualFromFormat = fromFormat;
        if (actualFromFormat == null) {
            actualFromFormat = FormatDetector.detectFormat(inputFile);
        }

        if ("UNKNOWN".equals(actualFromFormat)) {
            LoggerService.logFailure(
                inputFile.getAbsolutePath(),
                outputFile.getAbsolutePath(),
                actualFromFormat,
                toFormat,
                "Could not detect input file format"
            );
            return false;
        }

        String conversionKey = actualFromFormat + "_TO_" + toFormat;
        FileConverter converter = converters.get(conversionKey);

        if (converter == null) {
            LoggerService.logFailure(
                inputFile.getAbsolutePath(),
                outputFile.getAbsolutePath(),
                actualFromFormat,
                toFormat,
                "No converter available for " + actualFromFormat + " to " + toFormat
            );
            return false;
        }

        try {
            converter.convert(inputFile, outputFile);
            LoggerService.logSuccess(
                inputFile.getAbsolutePath(),
                outputFile.getAbsolutePath(),
                actualFromFormat,
                toFormat
            );
            return true;
        } catch (Exception e) {
            LoggerService.logFailure(
                inputFile.getAbsolutePath(),
                outputFile.getAbsolutePath(),
                actualFromFormat,
                toFormat,
                e.getMessage()
            );
            return false;
        }
    }

    /**
     * Gets all supported source formats.
     *
     * @return list of supported source formats
     */
    public List<String> getSupportedFromFormats() {
        List<String> formats = new ArrayList<>();
        for (String conversion : supportedConversions) {
            String fromFormat = conversion.split("_TO_")[0];
            if (!formats.contains(fromFormat)) {
                formats.add(fromFormat);
            }
        }
        return formats;
    }

    /**
     * Gets all supported target formats for a given source format.
     *
     * @param fromFormat the source format
     * @return list of supported target formats
     */
    public List<String> getSupportedToFormats(String fromFormat) {
        List<String> formats = new ArrayList<>();
        for (String conversion : supportedConversions) {
            if (conversion.startsWith(fromFormat + "_TO_")) {
                String toFormat = conversion.split("_TO_")[1];
                if (!formats.contains(toFormat)) {
                    formats.add(toFormat);
                }
            }
        }
        return formats;
    }

    /**
     * Checks if a conversion is supported.
     *
     * @param fromFormat the source format
     * @param toFormat the target format
     * @return true if conversion is supported
     */
    public boolean isConversionSupported(String fromFormat, String toFormat) {
        return supportedConversions.contains(fromFormat + "_TO_" + toFormat);
    }

    /**
     * Gets all supported conversions.
     *
     * @return list of all supported conversion keys
     */
    public List<String> getSupportedConversions() {
        return new ArrayList<>(supportedConversions);
    }

    /**
     * Gets the converter for a specific conversion.
     *
     * @param fromFormat the source format
     * @param toFormat the target format
     * @return the converter, or null if not found
     */
    public FileConverter getConverter(String fromFormat, String toFormat) {
        return converters.get(fromFormat + "_TO_" + toFormat);
    }

    /**
     * Gets all supported formats in the system.
     */
    private List<String> getSupportedFormats() {
        List<String> formats = new ArrayList<>();
        formats.add("CSV");
        formats.add("JSON");
        formats.add("XML");
        formats.add("EXCEL");
        formats.add("TEXT");
        formats.add("PDF");
        return formats;
    }
}
