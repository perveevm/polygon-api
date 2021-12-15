package ru.perveevm.polygon.api.entities;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
 */
public class ProblemInfo {
    private String inputFile;
    private String outputFile;
    private Boolean interactive;
    private Integer timeLimit;
    private Integer memoryLimit;

    public String getInputFile() {
        return inputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public Boolean getInteractive() {
        return interactive;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public Integer getMemoryLimit() {
        return memoryLimit;
    }
}
