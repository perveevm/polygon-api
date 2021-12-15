package ru.perveevm.polygon.api.entities;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
 */
public class ProblemFiles {
    private ProblemFile[] resourceFiles;
    private ProblemFile[] sourceFiles;
    private ProblemFile[] auxFiles;

    public ProblemFile[] getResourceFiles() {
        return resourceFiles;
    }

    public ProblemFile[] getSourceFiles() {
        return sourceFiles;
    }

    public ProblemFile[] getAuxFiles() {
        return auxFiles;
    }
}
