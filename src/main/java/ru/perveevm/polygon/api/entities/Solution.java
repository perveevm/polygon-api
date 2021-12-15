package ru.perveevm.polygon.api.entities;

import ru.perveevm.polygon.api.entities.enums.SolutionTag;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
 * <p>
 * Represents the problem solution.
 */
public class Solution {
    private String name;
    private Integer modificationTimeSeconds;
    private Integer length;
    private String sourceType;
    private SolutionTag tag;

    /**
     * @return Solution file name.
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
     * @return File size.
     */
    public Integer getLength() {
        return length;
    }

    /**
     * @return Solution source type (like file extension).
     */
    public String getSourceType() {
        return sourceType;
    }

    /**
     * @return Solution tag.
     */
    public SolutionTag getTag() {
        return tag;
    }
}
