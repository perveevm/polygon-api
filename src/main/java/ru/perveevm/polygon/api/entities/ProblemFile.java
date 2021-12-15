package ru.perveevm.polygon.api.entities;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
 */
public class ProblemFile {
    private String name;
    private Integer modificationTimeSeconds;
    private Integer length;
    private String sourceType;
    private ResourceAdvancedProperties resourceAdvancedProperties;

    public String getName() {
        return name;
    }

    public Integer getModificationTimeSeconds() {
        return modificationTimeSeconds;
    }

    public Integer getLength() {
        return length;
    }

    public String getSourceType() {
        return sourceType;
    }

    public ResourceAdvancedProperties getResourceAdvancedProperties() {
        return resourceAdvancedProperties;
    }
}
