package ru.perveevm.polygon.api.entities;

import ru.perveevm.polygon.api.entities.enums.PackageState;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
 */
public class ProblemPackage {
    private Integer id;
    private Integer revision;
    private Integer creationTimeSeconds;
    private PackageState state;
    private String comment;

    public Integer getId() {
        return id;
    }

    public Integer getRevision() {
        return revision;
    }

    public Integer getCreationTimeSeconds() {
        return creationTimeSeconds;
    }

    public PackageState getState() {
        return state;
    }

    public String getComment() {
        return comment;
    }
}
