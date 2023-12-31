package com.zdoryk.util;

public class Utils {

    public static boolean isValidEmail(String email) {
        // Regular expression pattern to match email addresses
        String pattern = "^[a-zA-Z0-9.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        return !email.matches(pattern) || (email.toLowerCase().endsWith(".ru") || email.toLowerCase().endsWith(".by"));
    }

}
