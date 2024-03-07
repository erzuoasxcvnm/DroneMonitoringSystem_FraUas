package main.java.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * The {@code Coding} class provides methods for encoding and decoding strings using Base64 encoding.
 * It uses the Java Base64 API to convert strings to and from Base64 representation.
 * <p>This class simplifies the process of encoding and decoding strings for use in various scenarios.</p>
 */
public class Coding {
    /**
     * Encodes the given string using Base64 encoding.
     *
     * @param value The string to be encoded.
     * @return The Base64-encoded representation of the input string.
     */
    public static String encode(String value) {
        try {
            byte[] encodedBytes = Base64.getEncoder().encode(value.getBytes(StandardCharsets.UTF_8));
            return new String(encodedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            Logs.error("Error occurred in Coding.encode() : " + e.getMessage());
            return null;
        }
    }

    /**
     * Decodes the given Base64-encoded string.
     *
     * @param value The Base64-encoded string to be decoded.
     * @return The decoded string from the Base64 representation.
     */
    public static String decode(String value) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(value.getBytes(StandardCharsets.UTF_8));
            return new String(decodedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            Logs.error("Error occurred in Coding.decode() : " + e.getMessage());
            return null;
        }
    }
}
