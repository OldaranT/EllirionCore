package com.ellirion.core.util;

public class StringHelper {

    /**
     * @param string The string to clean.
     * @param regex The regex to use.
     * @return Return the cleaned string.
     */
    public static String stringCleaner(String string, String regex) {
        return string.replaceAll(regex, "");
    }

    /**
     * @param strings The string Array to clean.
     * @param regex The regex to clean it with.
     * @return Return a cleaned string.
     */
    public static String[] stringArrayCleaner(String[] strings, String regex) {
        String[] result = new String[strings.length];
        for (int i = 0; i < strings.length; i++) {
            result[i] = stringCleaner(strings[i], regex);
        }
        return result;
    }

    /**
     * @param strings The string array to apply normal name casing to.
     * @return Return a cleaned string with normal namecasing.
     */
    public static String normalNameCasing(String[] strings) {
        return normalNameCasing(String.join(" ", strings));
    }

    /**
     * @param string The string to apply normal name casing to.
     * @return Return a cleaned string with normal namecasing.
     */
    public static String normalNameCasing(String string) {
        String result = "";
        string = stringCleaner(string, "[^a-zA-Z\\s]");
        string = string.toLowerCase();
        String[] words = string.split(" ");
        for (String word : words) {
            if (word.equals(" ")) {
                result += word;
                continue;
            }
            if (word.length() == 1 || word.equals("the")) {
                result += word + " ";
                continue;
            }
            result += word.replaceFirst(word.charAt(0) + "", Character.toUpperCase(word.charAt(0)) + "") + " ";
        }

        return result;
    }
}
