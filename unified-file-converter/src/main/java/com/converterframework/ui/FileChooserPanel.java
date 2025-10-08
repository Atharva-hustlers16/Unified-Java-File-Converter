package com.converterframework.ui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Panel for selecting input/output files and conversion format.
 */
public class FileChooserPanel extends JPanel {

    private final JTextField inputFileField;
    private final JComboBox<String> outputFormatComboBox;
    private final JButton convertButton;

    private File selectedFile;

    public FileChooserPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder("Select File and Format"));

        inputFileField = new JTextField(30);
        inputFileField.setEditable(false);

        JButton browseButton = new JButton("Browse...");
        browseButton.addActionListener(e -> browseForFile());

        outputFormatComboBox = new JComboBox<>();

        convertButton = new JButton("Convert");

        add(new JLabel("Input File:"));
        add(inputFileField);
        add(browseButton);
        add(new JLabel("Output Format:"));
        add(outputFormatComboBox);
        add(convertButton);
    }

    /**
     * Opens a file chooser to select an input file.
     */
    private void browseForFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            inputFileField.setText(selectedFile.getAbsolutePath());
        }
    }

    /**
     * Gets the selected input file.
     */
    public File getSelectedFile() {
        return selectedFile;
    }

    /**
     * Gets the selected output format.
     */
    public String getSelectedOutputFormat() {
        return (String) outputFormatComboBox.getSelectedItem();
    }

    /**
     * Gets the convert button.
     */
    public JButton getConvertButton() {
        return convertButton;
    }

    /**
     * Sets the available output formats.
     */
    public void setOutputFormats(String[] formats) {
        outputFormatComboBox.setModel(new DefaultComboBoxModel<>(formats));
    }
}
