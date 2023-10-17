package ru.perveevm.polygon.api.entities.enums;

/**
 * Represents checker test verdict.
 *
 * @author Perveev Mike (perveev_m@mail.ru)
 */
public enum CheckerTestVerdict {
    /**
     * Checker should return OK response.
     */
    OK,

    /**
     * Checker should return Wrong Answer response.
     */
    WRONG_ANSWER,

    /**
     * Checker should return Crashed response.
     */
    CRASHED,

    /**
     * Checker should return Presentation Error response.
     */
    PRESENTATION_ERROR
}
