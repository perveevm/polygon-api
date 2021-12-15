package ru.perveevm.polygon.api.entities;

import ru.perveevm.polygon.api.entities.enums.TestGroupFeedbackPolicy;
import ru.perveevm.polygon.api.entities.enums.TestGroupPointsPolicy;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
 */
public class TestGroup {
    private String name;
    private TestGroupPointsPolicy pointsPolicy;
    private TestGroupFeedbackPolicy feedbackPolicy;
    private String[] dependencies;

    public String getName() {
        return name;
    }

    public TestGroupPointsPolicy getPointsPolicy() {
        return pointsPolicy;
    }

    public TestGroupFeedbackPolicy getFeedbackPolicy() {
        return feedbackPolicy;
    }

    public String[] getDependencies() {
        return dependencies;
    }
}
