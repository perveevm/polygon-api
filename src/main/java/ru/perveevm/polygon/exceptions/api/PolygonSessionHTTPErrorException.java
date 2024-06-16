package ru.perveevm.polygon.exceptions.api;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * Thrown when a HTTP error occurred while sending POST request.
 *
 * @author Mike Perveev (perveev_m@mail.ru)
 */
public class PolygonSessionHTTPErrorException extends PolygonSessionException {
    /**
     * Constructs {@link PolygonSessionHTTPErrorException} using request URL and parameters.
     *
     * @param url        Request URL.
     * @param parameters A {@link List} of parameters.
     */
    public PolygonSessionHTTPErrorException(final String url, final List<NameValuePair> parameters) {
        super(getMessage(url, parameters));
    }

    /**
     * Constructs {@link PolygonSessionHTTPErrorException} using request URL, parameters and cause exception.
     *
     * @param url        Request URL.
     * @param parameters A {@link List} of parameters.
     * @param cause      Cause of this exception.
     */
    public PolygonSessionHTTPErrorException(final String url, final List<NameValuePair> parameters,
                                            final Throwable cause) {
        super(getMessage(url, parameters), cause);
    }

    /**
     * Constructs {@link PolygonSessionHTTPErrorException} using request URL and cause exception.
     *
     * @param url   Request URL.
     * @param cause Cause of this exception.
     */
    public PolygonSessionHTTPErrorException(final String url, final Throwable cause) {
        super(getMessage(url), cause);
    }
}
