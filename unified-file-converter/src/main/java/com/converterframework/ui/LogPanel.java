package com.converterframework.ui;

import com.converterframework.core.LoggerService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel for displaying conversion logs.
 */
public class LogPanel extends JPanel {

    private final JTable logTable;
    private final DefaultTableModel tableModel;

    public LogPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Conversion Log"));

        String[] columnNames = {"Date", "Input File", "Output File", "From", "To", "Status", "Message"};
        tableModel = new DefaultTableModel(columnNames, 0);
        logTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(logTable);
        add(scrollPane, BorderLayout.CENTER);

        refreshLogs();
    }

    /**
     * Refreshes the log display.
     */
    public void refreshLogs() {
        // Clear existing rows
        tableModel.setRowCount(0);

        // Get recent logs
        List<String[]> logs = LoggerService.getRecentLogs(100);

        // Add logs to table
        for (String[] log : logs) {
            tableModel.addRow(log);
        }
    }
}
