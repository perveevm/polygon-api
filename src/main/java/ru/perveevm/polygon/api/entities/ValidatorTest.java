package ru.perveevm.polygon.api.entities;

import ru.perveevm.polygon.api.entities.enums.ValidatorTestVerdict;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
 * <p>
 * Represents a validator’s test for the problem.
 */
public class ValidatorTest {
    private Integer index;
    private String input;
    private ValidatorTestVerdict expectedVerdict;
    private String testset;
    private String group;

    /**
     * @return Validator test index.
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @return Validator test input.
     */
    public String getInput() {
        return input;
    }

    /**
     * @return Expected verdict for the validator test.
     */
    public ValidatorTestVerdict getExpectedVerdict() {
        return expectedVerdict;
    }

    /**
     * @return Validator test set (may be <code>null</code>).
     */
    public String getTestset() {
        return testset;
    }

    /**
     * @return Validator test group (may be <code>null</code>).
     */
    public String getGroup() {
        return group;
    }
}