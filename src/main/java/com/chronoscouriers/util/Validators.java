package com.chronoscouriers.util;

public class Validators {

    public static void requireNonNullOrEmpty(String str, String fieldName) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty.");
        }
    }
    public static void requireNonNull(Object obj, String fieldName) {
        if (obj == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null.");
        }
    }
    public static void requirePositive(double number, String fieldName) {
        if (number <= 0) {
            throw new IllegalArgumentException(fieldName + " must be positive.");
        }
    }
}