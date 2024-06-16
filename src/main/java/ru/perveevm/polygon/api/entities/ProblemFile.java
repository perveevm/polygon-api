package ru.perveevm.polygon.api.entities;

/**
 * Represents a resource, source or aux file.
 *
 * @author Mike Perveev (perveev_m@mail.ru)
 */
public class ProblemFile {
    private String name;
    private Integer modificationTimeSeconds;
    private Integer length;
    private String sourceType;
    private ResourceAdvancedProperties resourceAdvancedProperties;

    /**
     * @return File name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Last modification time in unix format.
     */
    public Integer getModificationTimeSeconds() {
        return modificationTimeSeconds;
    }

    /**
     * @return File length.
     */
    public Integer getLength() {
        return length;
    }

    /**
     * @return Source file type. It is <code>null</code> for resources and aux files.
     */
    public String getSourceType() {
        return sourceType;
    }

    /**
     * @return Advanced properties for the file. It is <code>null</code> for sources and aux files.
     */
    public ResourceAdvancedProperties getResourceAdvancedProperties() {
        return resourceAdvancedProperties;
    }
}
