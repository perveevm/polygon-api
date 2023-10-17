package ru.perveevm.polygon.api.entities.enums;

/**
 * Represents a testing policy for a test group.
 *
 * @author Perveev Mike (perveev_m@mail.ru)
 */
public enum TestGroupPointsPolicy {
    /**
     * Score group if and only if all its tests passed.
     */
    COMPLETE_GROUP,
    /**
     * Score each test independently.
     */
    EACH_TEST
}
