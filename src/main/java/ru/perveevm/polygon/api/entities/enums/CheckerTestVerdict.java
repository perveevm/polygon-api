package ru.perveevm.polygon.api.entities.enums;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
 * <p>
 * Represents checker test verdict.
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