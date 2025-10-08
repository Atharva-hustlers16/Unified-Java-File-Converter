package com.converterframework.interfaces;

import java.io.File;

/**
 * Interface for file format converters.
 * Implementations should handle specific conversion types.
 */
public interface FileConverter {

    /**
     * Checks if this converter supports the given conversion.
     *
     * @param fromFormat the source format
     * @param toFormat the target format
     * @return true if this converter can handle the conversion
     */
    boolean supportsFormat(String fromFormat, String toFormat);

    /**
     * Performs the file conversion.
     *
     * @param inputFile the input file to convert
     * @param outputFile the output file to create
     * @throws Exception if conversion fails
     */
    void convert(File inputFile, File outputFile) throws Exception;

    /**
     * Gets the name of this converter.
     *
     * @return the converter name
     */
    String getConverterName();
}
