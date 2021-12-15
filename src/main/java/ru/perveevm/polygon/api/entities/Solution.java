package ru.perveevm.polygon.api.entities;

import ru.perveevm.polygon.api.entities.enums.SolutionTag;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
 */
public class Solution {
    private String name;
    private Integer modificationTimeSeconds;
    private Integer length;
    private String sourceType;
    private SolutionTag tag;

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

    public SolutionTag getTag() {
        return tag;
    }
}
