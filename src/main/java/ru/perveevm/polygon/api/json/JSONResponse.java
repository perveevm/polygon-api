package ru.perveevm.polygon.api.json;

import com.google.gson.JsonElement;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
 * <p>
 * Represents a JSON response.
 */
public class JSONResponse {
    private JSONResponseStatus status;
    private String comment;
    private JsonElement result;

    /**
     * @return Response status — either OK or FAILED.
     */
    public JSONResponseStatus getStatus() {
        return status;
    }

    /**
     * @return Server comment if status is FAILED or <code>null</code> otherwise.
     */
    public String getComment() {
        return comment;
    }

    /**
     * @return A JSON response if status is OK or <code>null</code> otherwise.
     */
    public JsonElement getResult() {
        return result;
    }
}
