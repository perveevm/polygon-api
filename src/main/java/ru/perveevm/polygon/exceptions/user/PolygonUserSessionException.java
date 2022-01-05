package ru.perveevm.polygon.exceptions.user;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
 */
public class PolygonUserSessionException extends Exception {
    public PolygonUserSessionException(final String message) {
        super(message);
    }

    public PolygonUserSessionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
