package ru.perveevm.polygon.api.entities.enums;

/**
 * Represents solution tag.
 *
 * @author Mike Perveev (perveev_m@mail.ru)
 */
public enum SolutionTag {
    /**
     * Solution is correct.
     */
    OK,
    /**
     * Solution fails.
     */
    RJ,
    /**
     * Time limit exceeded.
     */
    TL,
    /**
     * Time limit exceeded or correct.
     */
    TO,
    /**
     * Wrong answer.
     */
    WA,
    /**
     * Presentation error.
     */
    PE,
    /**
     * Memory limit exceeded.
     */
    ML,
    /**
     * Runtime error.
     */
    RE,
    /**
     * Main correct solution.
     */
    MA,
    /**
     * Do not run this solution.
     */
    NR,
    /**
     * Time or memory limit exceeded.
     */
    TM
}
