package ru.perveevm.polygon.api.entities.enums;

/**
 * Represents feedback policy of a test group.
 *
 * @author Mike Perveev (perveev_m@mail.ru)
 */
public enum TestGroupFeedbackPolicy {
    /**
     * Show all information in feedback.
     */
    COMPLETE,
    /**
     * Show information due to the first incorrect test.
     */
    ICPC,
    /**
     * Show only points.
     */
    POINTS,
    /**
     * Show nothing.
     */
    NONE
}
