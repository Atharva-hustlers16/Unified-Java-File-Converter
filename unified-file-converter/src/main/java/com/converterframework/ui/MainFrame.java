package com.converterframework.ui;

import com.converterframework.core.ConversionManager;
import com.converterframework.core.FormatDetector;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Main frame of the application.
 */
public class MainFrame extends JFrame {

    private final ConversionManager conversionManager;
    private final FileChooserPanel fileChooserPanel;
    private final LogPanel logPanel;
    private final StatusPanel statusPanel;

    public MainFrame(ConversionManager conversionManager) {
        this.conversionManager = conversionManager;

        setTitle("Unified File Format Converter");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create panels
        fileChooserPanel = new FileChooserPanel();
        logPanel = new LogPanel();
        statusPanel = new StatusPanel();

        // Layout
        setLayout(new BorderLayout());
        add(fileChooserPanel, BorderLayout.NORTH);
        add(logPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        // Add listeners
        fileChooserPanel.getConvertButton().addActionListener(e -> performConversion());

        // Populate output formats
        fileChooserPanel.setOutputFormats(new String[]{"JSON", "XML", "EXCEL", "PDF"});
    }

    /**
     * Performs the file conversion.
     */
    private void performConversion() {
        File inputFile = fileChooserPanel.getSelectedFile();
        if (inputFile == null) {
            JOptionPane.showMessageDialog(this, "Please select an input file.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String toFormat = fileChooserPanel.getSelectedOutputFormat();
        if (toFormat == null) {
            JOptionPane.showMessageDialog(this, "Please select an output format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String fromFormat = FormatDetector.detectFormat(inputFile);
        if ("UNKNOWN".equals(fromFormat)) {
            JOptionPane.showMessageDialog(this, "Could not detect the input file format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As");
        fileChooser.setSelectedFile(new File(inputFile.getParent(), "output." + toFormat.toLowerCase()));
        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File outputFile = fileChooser.getSelectedFile();

            statusPanel.showBusy(true);
            statusPanel.setStatus("Converting...");

            new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() {
                    return conversionManager.convertFile(inputFile, outputFile, fromFormat, toFormat);
                }

                @Override
                protected void done() {
                    try {
                        if (get()) {
                            statusPanel.setStatus("Conversion successful!");
                        } else {
                            statusPanel.setStatus("Conversion failed.");
                        }
                    } catch (Exception e) {
                        statusPanel.setStatus("Conversion failed: " + e.getMessage());
                    }
                    statusPanel.showBusy(false);
                    logPanel.refreshLogs();
                }
            }.execute();
        }
    }
}
