package com.ellirion.core.gamemanager.setup;

import lombok.Getter;

public class Step {

    @Getter private String message;

    /**
     * Create a setup step.
     * @param message the message of this step
     */
    public Step(final String message) {
        this.message = message;
    }
}
