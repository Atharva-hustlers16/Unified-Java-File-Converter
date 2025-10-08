package com.converterframework.converters;

import com.converterframework.interfaces.FileConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Converter for Text to PDF format.
 */
public class TextToPDFConverter implements FileConverter {

    @Override
    public boolean supportsFormat(String from, String to) {
        return "TEXT".equals(from) && "PDF".equals(to);
    }

    @Override
    public void convert(File inputFile, File outputFile) throws IOException {
        if (!inputFile.exists() || !inputFile.canRead()) {
            throw new IllegalArgumentException("Input file does not exist or cannot be read: " + inputFile.getPath());
        }

        try (PdfWriter writer = new PdfWriter(outputFile);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            String content = new String(Files.readAllBytes(inputFile.toPath()));
            document.add(new Paragraph(content));
        }
    }

    @Override
    public String getConverterName() {
        return "Text to PDF Converter";
    }
}
