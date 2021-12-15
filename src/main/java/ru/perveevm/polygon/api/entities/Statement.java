package ru.perveevm.polygon.api.entities;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
 */
public class Statement {
    private String encoding;
    private String name;
    private String legend;
    private String input;
    private String output;
    private String scoring;
    private String notes;
    private String tutorial;

    public String getEncoding() {
        return encoding;
    }

    public String getName() {
        return name;
    }

    public String getLegend() {
        return legend;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    public String getScoring() {
        return scoring;
    }

    public String getNotes() {
        return notes;
    }

    public String getTutorial() {
        return tutorial;
    }
}
