package ru.perveevm.polygon.api.entities.enums;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
 * <p>
 * Represents a type of a resource.
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
