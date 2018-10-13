package com.ellirion.core.utils;

import com.ellirion.core.EllirionCore;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

public class LoggingUtil {

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
}
