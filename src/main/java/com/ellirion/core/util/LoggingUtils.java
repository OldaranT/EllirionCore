package com.ellirion.core.util;

import com.ellirion.core.EllirionCore;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

public class LoggingUtils {

    private static Logger LOGGER = EllirionCore.getINSTANCE().getLogger();

    /**
     * This logs the stack trace to the console.
     * @param exception The exception that has the stacktrace.
     */
    public static void printStackTrace(Exception exception) {
        StringWriter errors = new StringWriter();
        exception.printStackTrace(new PrintWriter(errors));
        LOGGER.severe(errors.toString());
    }

    /**
     * This makes it able to send an info message to the console.
     * @param message The message you want to send.
     */
    public static void printMessage(String message) {
        LOGGER.info(message);
    }
}
