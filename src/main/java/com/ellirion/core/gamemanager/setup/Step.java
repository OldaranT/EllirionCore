package com.ellirion.core.gamemanager.setup;

import lombok.Getter;

import java.util.function.Function;

public class Step {

    @Getter private String message;
    @Getter private Boolean requirement;
    private Function<Object, Boolean> req;

    /**
     * Create a setup step.
     * @param message the message of this step
     */
    public Step(final String message) {
        this.message = message;
        requirement = false;
    }

    /**
     * Create a setup step with a requirement.
     * @param message the message of this step
     * @param requirement the requirement that needs to be completed in this step
     */
    public Step(final String message, final Function<Object, Boolean> requirement) {
        this(message);
        this.requirement = true;
        req = requirement;
    }

    /**
     * Check if the requirement of this step has been completed.
     * @param t object to check for the requirement (dummy)
     * @return whether the requirement has been completed
     */
    public Boolean requirementComplete(Object t) {
        return !requirement || req.apply(t);
    }
}
