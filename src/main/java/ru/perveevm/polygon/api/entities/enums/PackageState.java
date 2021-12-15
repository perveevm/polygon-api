package ru.perveevm.polygon.api.entities.enums;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
 * <p>
 * Represents package state.
 */
public enum PackageState {
    /**
     * Package is pending to be built.
     */
    PENDING,
    /**
     * Package is being built.
     */
    RUNNING,
    /**
     * Package was built successfully.
     */
    READY,
    /**
     * Failed to build a package.
     */
    FAILED
}
