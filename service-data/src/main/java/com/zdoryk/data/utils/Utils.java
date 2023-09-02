package com.zdoryk.data.utils;

public class Utils {

    public static boolean isValidEmail(String email) {
        String pattern = "^[a-zA-Z0-9._+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        return !email.matches(pattern)
                || (email.toLowerCase().endsWith(".ru")
                || email.toLowerCase().endsWith(".by"));
    }

}
