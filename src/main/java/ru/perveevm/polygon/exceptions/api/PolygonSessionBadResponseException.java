package ru.perveevm.polygon.exceptions.api;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * Thrown when the response is bad: either the response code is not 200, or the JSON is invalid.
 *
 * @author Perveev Mike (perveev_m@mail.ru)
 */
public class PolygonSessionBadResponseException extends PolygonSessionException {
    /**
     * Creates a {@link PolygonSessionBadResponseException} using request URL, parameters and response HTTP code.
     *
     * @param url          Request URL.
     * @param parameters   Request parameters.
     * @param responseCode Response HTTP code.
     */
    public PolygonSessionBadResponseException(final String url, final List<NameValuePair> parameters,
                                              final int responseCode) {
        super(getMessage(url, parameters) + String.format(", response code is %d", responseCode));
    }

    /**
     * Creates a {@link PolygonSessionBadResponseException} using request URL, parameters, response HTTP code
     * and cause exception.
     *
     * @param url          Request URL.
     * @param parameters   Request parameters.
     * @param responseCode Response HTTP code.
     * @param cause        Cause of this exception.
     */
    public PolygonSessionBadResponseException(final String url, final List<NameValuePair> parameters,
                                              final int responseCode,
                                              final Throwable cause) {
        super(getMessage(url, parameters) + String.format(", response code is %d", responseCode), cause);
    }

    /**
     * Creates a {@link PolygonSessionBadResponseException} using request URL and parameters.
     *
     * @param url        Request URL.
     * @param parameters Request parameters.
     */
    public PolygonSessionBadResponseException(final String url, final List<NameValuePair> parameters) {
        super(getMessage(url, parameters) + ", cannot parse response");
    }

    /**
     * Creates a {@link PolygonSessionBadResponseException} using request URL, parameters and cause exception.
     *
     * @param url        Request URL.
     * @param parameters Request parameters.
     * @param cause      Cause of this exception.
     */
    public PolygonSessionBadResponseException(final String url, final List<NameValuePair> parameters,
                                              final Throwable cause) {
        super(getMessage(url, parameters) + ", cannot parse response", cause);
    }
}
