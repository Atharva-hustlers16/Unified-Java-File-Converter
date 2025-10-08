package com.converterframework.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Panel for displaying conversion status and progress.
 */
public class StatusPanel extends JPanel {

    private final JLabel statusLabel;
    private final JProgressBar progressBar;

    public StatusPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        statusLabel = new JLabel("Ready");
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        progressBar.setStringPainted(true);

        add(statusLabel, BorderLayout.CENTER);
        add(progressBar, BorderLayout.EAST);
    }

    /**
     * Sets the status message.
     *
     * @param message the message to display
     */
    public void setStatus(String message) {
        statusLabel.setText(message);
    }

    /**
     * Sets the progress bar value.
     *
     * @param value the progress value (0-100)
     */
    public void setProgress(int value) {
        progressBar.setValue(value);
        progressBar.setIndeterminate(false);
    }

    /**
     * Shows an indeterminate progress bar.
     */
    public void showBusy(boolean busy) {
        progressBar.setIndeterminate(busy);
        progressBar.setString(busy ? "Converting..." : "");
    }

    /**
     * Resets the status panel to its initial state.
     */
    public void reset() {
        setStatus("Ready");
        setProgress(0);
        showBusy(false);
    }
}
