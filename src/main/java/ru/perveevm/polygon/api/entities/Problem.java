package ru.perveevm.polygon.api.entities;

import ru.perveevm.polygon.api.entities.enums.ProblemAccessType;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
 */
public class Problem {
    private Integer id;
    private String owner;
    private String name;
    private Boolean deleted;
    private Boolean favourite;
    private ProblemAccessType accessType;
    private Integer revision;
    private Integer latestPackage;
    private Boolean modified;

    public Integer getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public Boolean getFavourite() {
        return favourite;
    }

    public ProblemAccessType getAccessType() {
        return accessType;
    }

    public Integer getRevision() {
        return revision;
    }

    public Integer getLatestPackage() {
        return latestPackage;
    }

    public Boolean getModified() {
        return modified;
    }
}
