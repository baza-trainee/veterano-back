package com.zdoryk.data.utils;

public class Utils {

    public static boolean isValidEmail(String email) {
        // Regular expression pattern to match email addresses
        String pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        // Check if the email matches the pattern and doesn't end with ".ru" or ".by"
        return !email.matches(pattern) || (email.toLowerCase().endsWith(".ru") || email.toLowerCase().endsWith(".by"));
    }

}
