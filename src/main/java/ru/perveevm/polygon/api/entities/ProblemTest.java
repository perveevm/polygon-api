package ru.perveevm.polygon.api.entities;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
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

    public Integer getIndex() {
        return index;
    }

    public Boolean getManual() {
        return manual;
    }

    public String getInput() {
        return input;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getUseInStatements() {
        return useInStatements;
    }

    public String getScriptLine() {
        return scriptLine;
    }

    public String getGroup() {
        return group;
    }

    public Double getPoints() {
        return points;
    }

    public String getInputForStatement() {
        return inputForStatement;
    }

    public String getOutputForStatement() {
        return outputForStatement;
    }

    public Boolean getVerifyInputOutputForStatements() {
        return verifyInputOutputForStatements;
    }
}
