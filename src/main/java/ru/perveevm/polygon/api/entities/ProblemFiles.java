package ru.perveevm.polygon.api.entities;

/**
 * Represents all problem files.
 *
 * @author Perveev Mike (perveev_m@mail.ru)
 */
public class ProblemFiles {
    private ProblemFile[] resourceFiles;
    private ProblemFile[] sourceFiles;
    private ProblemFile[] auxFiles;

    /**
     * @return An array of resource files.
     */
    public ProblemFile[] getResourceFiles() {
        return resourceFiles;
    }

    /**
     * @return An array of source files.
     */
    public ProblemFile[] getSourceFiles() {
        return sourceFiles;
    }

    /**
     * @return An array of aux files.
     */
    public ProblemFile[] getAuxFiles() {
        return auxFiles;
    }
}
