package com.converterframework.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Utility class for various file operations.
 */
public class FileUtils {

    /**
     * Creates a backup of the original file before conversion.
     *
     * @param originalFile the file to backup
     * @return the backup file, or null if backup failed
     */
    public static File createBackup(File originalFile) {
        if (originalFile == null || !originalFile.exists()) {
            return null;
        }

        try {
            Path originalPath = originalFile.toPath();
            String fileName = originalFile.getName();
            String extension = getFileExtension(fileName);
            String baseName = fileName.substring(0, fileName.length() - extension.length() - 1);

            Path backupPath = Paths.get(originalFile.getParent(),
                baseName + "_backup_" + System.currentTimeMillis() + "." + extension);

            Files.copy(originalPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
            return backupPath.toFile();
        } catch (IOException e) {
            System.err.println("Failed to create backup: " + e.getMessage());
            return null;
        }
    }

    /**
     * Validates that a file exists and is readable.
     *
     * @param file the file to validate
     * @return true if file is valid
     */
    public static boolean isValidInputFile(File file) {
        return file != null && file.exists() && file.isFile() && file.canRead();
    }

    /**
     * Validates that an output file can be created.
     *
     * @param file the output file
     * @return true if output file is valid
     */
    public static boolean isValidOutputFile(File file) {
        if (file == null) return false;

        // Check if parent directory exists and is writable
        File parentDir = file.getParentFile();
        return parentDir != null && parentDir.exists() && parentDir.isDirectory() && parentDir.canWrite();
    }

    /**
     * Gets the file extension from a filename.
     *
     * @param filename the filename
     * @return the file extension (without the dot)
     */
    public static String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }

        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0 && lastDotIndex < filename.length() - 1
            ? filename.substring(lastDotIndex + 1)
            : "";
    }

    /**
     * Gets the filename without extension.
     *
     * @param filename the filename
     * @return the filename without extension
     */
    public static String getFilenameWithoutExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return filename;
        }

        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0 ? filename.substring(0, lastDotIndex) : filename;
    }

    /**
     * Creates a file with the specified content.
     *
     * @param file the file to create
     * @param content the content to write
     * @throws IOException if file creation fails
     */
    public static void createFileWithContent(File file, String content) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }

        Files.write(file.toPath(), content.getBytes());
    }

    /**
     * Safely deletes a file if it exists.
     *
     * @param file the file to delete
     * @return true if file was deleted or didn't exist
     */
    public static boolean safeDelete(File file) {
        try {
            return Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            System.err.println("Failed to delete file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets the size of a file in bytes.
     *
     * @param file the file
     * @return the file size, or -1 if file doesn't exist
     */
    public static long getFileSize(File file) {
        if (file == null || !file.exists()) {
            return -1;
        }
        return file.length();
    }

    /**
     * Formats file size in human-readable format.
     *
     * @param bytes the size in bytes
     * @return formatted size string
     */
    public static String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
}
