package com.converterframework;

import com.converterframework.core.ConversionManager;
import com.converterframework.converters.*;
import com.converterframework.ui.MainFrame;

import javax.swing.*;

/**
 * Main class to launch the application.
 */
public class Main {

    public static void main(String[] args) {
        // Create and configure the conversion manager
        ConversionManager conversionManager = new ConversionManager();
        conversionManager.registerConverter(new CSVToJSONConverter());
        conversionManager.registerConverter(new JSONToCSVConverter());
        conversionManager.registerConverter(new JSONToXMLConverter());
        conversionManager.registerConverter(new CSVToExcelConverter());
        conversionManager.registerConverter(new TextToPDFConverter());

        // Launch the GUI
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(conversionManager);
            mainFrame.setVisible(true);
        });
    }
}
