package ru.perveevm.polygon.api.entities;

import ru.perveevm.polygon.api.entities.enums.PackageState;

/**
 * Represents problem package.
 *
 * @author Perveev Mike (perveev_m@mail.ru)
 */
public class ProblemPackage {
    private Integer id;
    private Integer revision;
    private Integer creationTimeSeconds;
    private PackageState state;
    private String comment;

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
}
