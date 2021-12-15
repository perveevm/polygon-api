package ru.perveevm.polygon.api.json;

import com.google.gson.JsonElement;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
 */
public class JSONResponse {
    private JSONResponseStatus status;
    private String comment;
    private JsonElement result;

    public JSONResponseStatus getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public JsonElement getResult() {
        return result;
    }
}
