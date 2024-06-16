package ru.perveevm.polygon.api.entities.enums;

/**
 * Represents package state.
 *
 * @author Mike Perveev (perveev_m@mail.ru)
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
