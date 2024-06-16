package ru.perveevm.polygon.exceptions.api;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * Thrown when the response JSON status is FAILED.
 *
 * @author Mike Perveev (perveev_m@mail.ru)
 */
public class PolygonSessionFailedRequestException extends PolygonSessionException {
    /**
     * Constructs {@link PolygonSessionFailedRequestException} using request URL, parameters and response API comment.
     *
     * @param url        Request URL.
     * @param parameters A {@link List} of parameters.
     * @param comment    A response comment from JSON.
     */
    public PolygonSessionFailedRequestException(final String url, final List<NameValuePair> parameters,
                                                final String comment) {
        super(getMessage(url, parameters) + String.format(" returned status is FAILED, comment: %s", comment));
    }

    /**
     * Constructs {@link PolygonSessionFailedRequestException} using request URL, parameters, response API comment and cause exception.
     *
     * @param url        Request URL.
     * @param parameters A {@link List} of parameters.
     * @param comment    A response comment from JSON.
     * @param cause      An exception cause.
     */
    public PolygonSessionFailedRequestException(final String url, final List<NameValuePair> parameters,
                                                final String comment, final Throwable cause) {
        super(getMessage(url, parameters) + String.format(" returned status is FAILED, comment: %s", comment), cause);
    }

    /**
     * Constructs {@link PolygonSessionFailedRequestException} using request URL and response API comment.
     *
     * @param url     Request URL.
     * @param comment A response comment from JSON.
     */
    public PolygonSessionFailedRequestException(final String url, final String comment) {
        super(getMessage(url) + String.format(" returned status is FAILED, comment: %s", comment));
    }
}
