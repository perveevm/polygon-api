package ru.perveevm.polygon.api.entities.enums;

/**
 * Represents user's access type to the problem.
 *
 * @author Mike Perveev (perveev_m@mail.ru)
 */
public enum ProblemAccessType {
    /**
     * Read-only access.
     */
    READ,
    /**
     * Read and write access.
     */
    WRITE,
    /**
     * Current user is a problem owner.
     */
    OWNER
}
