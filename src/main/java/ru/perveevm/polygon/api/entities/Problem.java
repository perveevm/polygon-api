package ru.perveevm.polygon.api.entities;

import ru.perveevm.polygon.api.entities.enums.ProblemAccessType;

/**
 * Represents problem of a contest.
 *
 * @author Mike Perveev (perveev_m@mail.ru)
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

    /**
     * @return Problem ID.
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return Problem owner handle.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @return Problem name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Is <code>true</code> if problem was deleted.
     */
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * @return Is <code>true</code> is problem is favourite.
     */
    public Boolean getFavourite() {
        return favourite;
    }

    /**
     * @return User's access type for the problem.
     */
    public ProblemAccessType getAccessType() {
        return accessType;
    }

    /**
     * @return Problem's current revision.
     */
    public Integer getRevision() {
        return revision;
    }

    /**
     * @return Latest revision with available package. Can be <code>null</code>.
     */
    public Integer getLatestPackage() {
        return latestPackage;
    }

    /**
     * @return Is <code>true</code> if the problem was modified.
     */
    public Boolean getModified() {
        return modified;
    }
}
