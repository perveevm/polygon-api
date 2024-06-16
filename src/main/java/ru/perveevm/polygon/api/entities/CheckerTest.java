package ru.perveevm.polygon.api.entities;

import ru.perveevm.polygon.api.entities.enums.CheckerTestVerdict;

/**
 * Represents a checker’s test for the problem.
 *
 * @author Mike Perveev (perveev_m@mail.ru)
 */
public class CheckerTest {
    private Integer index;
    private String input;
    private String output;
    private String answer;
    private CheckerTestVerdict expectedVerdict;

    /**
     * @return Checker test index.
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @return Checker test input.
     */
    public String getInput() {
        return input;
    }

    /**
     * @return Checker test output.
     */
    public String getOutput() {
        return output;
    }

    /**
     * @return Checker test answer.
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * @return Expected verdict for the checker’s test.
     */
    public CheckerTestVerdict getExpectedVerdict() {
        return expectedVerdict;
    }
}
