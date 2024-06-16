package ru.perveevm.polygon.api.entities;

import ru.perveevm.polygon.api.entities.enums.TestGroupFeedbackPolicy;
import ru.perveevm.polygon.api.entities.enums.TestGroupPointsPolicy;

/**
 * Represents test group in a problem.
 *
 * @author Mike Perveev (perveev_m@mail.ru)
 */
public class TestGroup {
    private String name;
    private TestGroupPointsPolicy pointsPolicy;
    private TestGroupFeedbackPolicy feedbackPolicy;
    private String[] dependencies;

    /**
     * @return Group name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Points giving policy.
     */
    public TestGroupPointsPolicy getPointsPolicy() {
        return pointsPolicy;
    }

    /**
     * @return Shown feedback policy.
     */
    public TestGroupFeedbackPolicy getFeedbackPolicy() {
        return feedbackPolicy;
    }

    /**
     * @return An array of other groups' names — the dependencies for current group.
     */
    public String[] getDependencies() {
        return dependencies;
    }
}
