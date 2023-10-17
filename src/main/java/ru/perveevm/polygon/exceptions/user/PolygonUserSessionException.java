package ru.perveevm.polygon.exceptions.user;

/**
 * This class describes errors that can happen while using {@link ru.perveevm.polygon.user.PolygonUserSession}
 *
 * @author Perveev Mike (perveev_m@mail.ru)
 */
public class PolygonUserSessionException extends Exception {
    /**
     * Constructs {@link PolygonUserSessionException} using error message.
     *
     * @param message Error message.
     */
    public PolygonUserSessionException(final String message) {
        super(message);
    }

    /**
     * Constructs {@link PolygonUserSessionException} using error message and cause.
     *
     * @param message Error message.
     * @param cause   Cause exception.
     */
    public PolygonUserSessionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
