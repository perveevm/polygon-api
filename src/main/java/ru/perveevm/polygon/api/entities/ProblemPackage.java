package ru.perveevm.polygon.api.entities;

import ru.perveevm.polygon.api.entities.enums.PackageState;

/**
 * Represents problem package.
 *
 * @author Mike Perveev (perveev_m@mail.ru)
 */
public class ProblemPackage {
    private Integer id;
    private Integer revision;
    private Integer creationTimeSeconds;
    private PackageState state;
    private String comment;
    private String type;

    /**
     * @return Package ID.
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return Revision of a problem for which the package was created.
     */
    public Integer getRevision() {
        return revision;
    }

    /**
     * @return Package creation time in unix format.
     */
    public Integer getCreationTimeSeconds() {
        return creationTimeSeconds;
    }

    /**
     * @return Current package state.
     */
    public PackageState getState() {
        return state;
    }

    /**
     * @return Package comment.
     */
    public String getComment() {
        return comment;
    }

    /**
     * @return Type of the package: "standard", "linux" or "windows". Standard packages don't contain generated tests,
     * but contain windows executables and scripts for windows and linux via wine. Linux packages contain generated
     * tests, but don't contain compiled binaries. Windows packages contain generated tests and compiled binaries
     * for Windows.
     */
    public String getType() {
        return type;
    }
}
