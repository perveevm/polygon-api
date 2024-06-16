package ru.perveevm.polygon.exceptions.api;

/**
 * Thrown when Polygon API does not respond with correct JSON object.
 *
 * @author Mike Perveev (perveev_m@mail.ru)
 */
public class PolygonSessionAPIUnavailableException extends PolygonSessionException {
    public PolygonSessionAPIUnavailableException() {
        super("Polygon API is unavailable now, retry the request later");
    }
}
