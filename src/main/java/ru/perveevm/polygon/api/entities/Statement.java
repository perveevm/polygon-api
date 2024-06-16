package ru.perveevm.polygon.api.entities;

/**
 * Represents problem statements.
 *
 * @author Mike Perveev (perveev_m@mail.ru)
 */
public class Statement {
    private String encoding;
    private String name;
    private String legend;
    private String input;
    private String output;
    private String scoring;
    private String interaction;
    private String notes;
    private String tutorial;

    /**
     * @return Statements encoding.
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * @return Problem name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Problem legend.
     */
    public String getLegend() {
        return legend;
    }

    /**
     * @return Problem input format.
     */
    public String getInput() {
        return input;
    }

    /**
     * @return Problem output format.
     */
    public String getOutput() {
        return output;
    }

    /**
     * @return Problem scoring description.
     */
    public String getScoring() {
        return scoring;
    }

    /**
     * @return Problem’s interaction protocol (only for interactive problems).
     */
    public String getInteraction() {
        return interaction;
    }

    /**
     * @return Problem notes.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @return Problem tutorial.
     */
    public String getTutorial() {
        return tutorial;
    }
}
