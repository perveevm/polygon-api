package ru.perveevm.polygon.api.entities.enums;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
 * <p>
 * Represents user's access type to the problem.
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
