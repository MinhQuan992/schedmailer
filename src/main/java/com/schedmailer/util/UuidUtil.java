package com.schedmailer.util;

import com.schedmailer.exception.InvalidUuidFormatException;

import java.util.UUID;

/** Utility class for UUID operations */
public class UuidUtil {
    private UuidUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Converts a string ID to UUID, throwing a user-friendly exception for invalid formats
     *
     * @param id the ID string to convert
     * @return the parsed UUID
     * @throws InvalidUuidFormatException if the ID is not a valid UUID format
     */
    public static UUID parseUuid(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidUuidFormatException("Invalid UUID format: " + id);
        }
    }
}
