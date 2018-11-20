package com.ellirion.core.database.util;

import com.ellirion.core.util.Logging;

public class GenericTryCatch {

    /**
     * This function does the try and catch for every DAO to prevent duplicated code.
     * @param toTry The code to try and run.
     * @return Return the result of the operation.
     */
    public static boolean tryCatch(Runnable toTry) {
        try {
            toTry.run();
            return true;
        } catch (Exception exception) {
            Logging.printStackTrace(exception);
            return false;
        }
    }
}
