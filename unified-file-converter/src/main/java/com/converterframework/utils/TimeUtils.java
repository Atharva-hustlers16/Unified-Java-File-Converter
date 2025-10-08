package com.converterframework.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for time-related operations.
 */
public class TimeUtils {

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter TIME_ONLY_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Gets the current date and time as a formatted string.
     *
     * @return current timestamp
     */
    public static String getCurrentTimestamp() {
        return LocalDateTime.now().format(DEFAULT_FORMATTER);
    }

    /**
     * Gets the current time only as a formatted string.
     *
     * @return current time
     */
    public static String getCurrentTime() {
        return LocalDateTime.now().format(TIME_ONLY_FORMATTER);
    }

    /**
     * Formats a LocalDateTime object.
     *
     * @param dateTime the date time to format
     * @param pattern the pattern to use
     * @return formatted date time string
     */
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Formats a LocalDateTime object using the default pattern.
     *
     * @param dateTime the date time to format
     * @return formatted date time string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return formatDateTime(dateTime, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Calculates the duration between two timestamps in milliseconds.
     *
     * @param startTime the start time
     * @param endTime the end time
     * @return duration in milliseconds
     */
    public static long getDurationInMillis(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            return 0;
        }
        return ChronoUnit.MILLIS.between(startTime, endTime);
    }

    /**
     * Formats a duration in milliseconds to a human-readable string.
     *
     * @param millis the duration in milliseconds
     * @return formatted duration string
     */
    public static String formatDuration(long millis) {
        if (millis < 1000) {
            return millis + "ms";
        }

        long seconds = millis / 1000;
        if (seconds < 60) {
            return seconds + "s";
        }

        long minutes = seconds / 60;
        if (minutes < 60) {
            return minutes + "m " + (seconds % 60) + "s";
        }

        long hours = minutes / 60;
        return hours + "h " + (minutes % 60) + "m";
    }

    /**
     * Gets the elapsed time since a given timestamp.
     *
     * @param startTime the start time
     * @return elapsed time as formatted string
     */
    public static String getElapsedTime(LocalDateTime startTime) {
        if (startTime == null) {
            return "Unknown";
        }
        return formatDuration(getDurationInMillis(startTime, LocalDateTime.now()));
    }

    /**
     * Checks if a timestamp is within the last N minutes.
     *
     * @param timestamp the timestamp to check
     * @param minutes the number of minutes
     * @return true if timestamp is within the last N minutes
     */
    public static boolean isWithinLastMinutes(LocalDateTime timestamp, int minutes) {
        if (timestamp == null) {
            return false;
        }
        return ChronoUnit.MINUTES.between(timestamp, LocalDateTime.now()) <= minutes;
    }

    /**
     * Parses a timestamp string using the default format.
     *
     * @param timestamp the timestamp string
     * @return LocalDateTime object, or null if parsing fails
     */
    public static LocalDateTime parseTimestamp(String timestamp) {
        try {
            return LocalDateTime.parse(timestamp, DEFAULT_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets a timestamp for a given number of minutes ago.
     *
     * @param minutes the number of minutes ago
     * @return timestamp from N minutes ago
     */
    public static LocalDateTime getTimestampMinutesAgo(int minutes) {
        return LocalDateTime.now().minusMinutes(minutes);
    }
}
