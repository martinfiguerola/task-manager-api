package com.martin.taskmanager.model;

public enum Status {
    PENDING,
    IN_PROGRESS,
    DONE;

    public boolean canTransitionTo(Status next) {
        return next.ordinal() >= this.ordinal();
    }
}
