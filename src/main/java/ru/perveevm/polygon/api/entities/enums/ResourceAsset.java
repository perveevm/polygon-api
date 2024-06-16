package ru.perveevm.polygon.api.entities.enums;

/**
 * Represents a type of the resource.
 *
 * @author Mike Perveev (perveev_m@mail.ru)
 */
public enum ResourceAsset {
    /**
     * Validator — script that validates input test files.
     */
    VALIDATOR,
    /**
     * Interactor — script that interacts with a solution.
     */
    INTERACTOR,
    /**
     * Checker — script that checks the correctness of an answer.
     */
    CHECKER,
    /**
     * Solution of a problem.
     */
    SOLUTION
}
