package ru.perveevm.polygon.api.entities;

/**
 * Represents problem general information.
 *
 * @author Mike Perveev (perveev_m@mail.ru)
 */
public class ProblemInfo {
    private String inputFile;
    private String outputFile;
    private Boolean interactive;
    private Integer timeLimit;
    private Integer memoryLimit;

    /**
     * @return Input file name.
     */
    public String getInputFile() {
        return inputFile;
    }

    /**
     * @return Output file name.
     */
    public String getOutputFile() {
        return outputFile;
    }

    /**
     * @return Is <code>true</code> if the problem is interactive.
     */
    public Boolean getInteractive() {
        return interactive;
    }

    /**
     * @return Time limit in milliseconds.
     */
    public Integer getTimeLimit() {
        return timeLimit;
    }

    /**
     * @return Memory limit in megabytes.
     */
    public Integer getMemoryLimit() {
        return memoryLimit;
    }
}
