package ru.perveevm.polygon.api.entities;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
 * <p>
 * Represents a test in a problem.
 */
public class ProblemTest {
    private Integer index;
    private Boolean manual;
    private String input;
    private String description;
    private Boolean useInStatements;
    private String scriptLine;
    private String group;
    private Double points;
    private String inputForStatement;
    private String outputForStatement;
    private Boolean verifyInputOutputForStatements;

    /**
     * @return Test index.
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @return Is <code>true</code> if the test is manual or <code>false</code> if it is generated using a generator.
     */
    public Boolean getManual() {
        return manual;
    }

    /**
     * @return Test input data. It is <code>null</code> if test is generated.
     */
    public String getInput() {
        return input;
    }

    /**
     * @return Test description, <code>null</code> for generated tests.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return Is <code>true</code> if this test is used in statements.
     */
    public Boolean getUseInStatements() {
        return useInStatements;
    }

    /**
     * @return Script line for generating the test, <code>null</code> for manual tests.
     */
    public String getScriptLine() {
        return scriptLine;
    }

    /**
     * @return Group name, can be <code>null</code>.
     */
    public String getGroup() {
        return group;
    }

    /**
     * @return Points assigned to the test, can be <code>null</code>.
     */
    public Double getPoints() {
        return points;
    }

    /**
     * @return Input redeclaration for statements, can be <code>null</code>.
     */
    public String getInputForStatement() {
        return inputForStatement;
    }

    /**
     * @return Output redeclaration for statements, can be <code>null</code>.
     */
    public String getOutputForStatement() {
        return outputForStatement;
    }

    /**
     * @return Is <code>true</code> if input/output redeclaration should be checked. Can be <code>null</code>.
     */
    public Boolean getVerifyInputOutputForStatements() {
        return verifyInputOutputForStatements;
    }
}
