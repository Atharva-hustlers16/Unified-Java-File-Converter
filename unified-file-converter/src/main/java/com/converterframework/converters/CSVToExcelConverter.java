package com.converterframework.converters;

import com.converterframework.interfaces.FileConverter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * Converter for CSV to Excel (XLSX) format.
 */
public class CSVToExcelConverter implements FileConverter {

    @Override
    public boolean supportsFormat(String from, String to) {
        return "CSV".equals(from) && "EXCEL".equals(to);
    }

    @Override
    public void convert(File inputFile, File outputFile) throws IOException {
        if (!inputFile.exists() || !inputFile.canRead()) {
            throw new IllegalArgumentException("Input file does not exist or cannot be read: " + inputFile.getPath());
        }

        List<String> lines = Files.readAllLines(inputFile.toPath());
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Input file is empty");
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");

            for (int i = 0; i < lines.size(); i++) {
                String[] values = lines.get(i).split(","); // Simple CSV split
                Row row = sheet.createRow(i);

                for (int j = 0; j < values.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(values[j]);
                }
            }

            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                workbook.write(fos);
            }
        }
    }

    @Override
    public String getConverterName() {
        return "CSV to Excel Converter";
    }
}
