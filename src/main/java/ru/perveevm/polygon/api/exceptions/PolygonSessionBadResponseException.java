package ru.perveevm.polygon.api.exceptions;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
 * <p>
 * Thrown when the response is bad: either the response code is not 200, or the JSON is invalid.
 */
public class PolygonSessionBadResponseException extends PolygonSessionException {
    /**
     * @param url          Request URL.
     * @param parameters   Request parameters.
     * @param responseCode Response HTTP code.
     */
    public PolygonSessionBadResponseException(final String url, final List<NameValuePair> parameters,
                                              final int responseCode) {
        super(getMessage(url, parameters) + String.format(", response code is %d", responseCode));
    }

    /**
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
     * @param url        Request URL.
     * @param parameters Request parameters.
     */
    public PolygonSessionBadResponseException(final String url, final List<NameValuePair> parameters) {
        super(getMessage(url, parameters) + ", cannot parse response");
    }

    /**
     * @param url        Request URL.
     * @param parameters Request parameters.
     * @param cause      Cause of this exception.
     */
    public PolygonSessionBadResponseException(final String url, final List<NameValuePair> parameters,
                                              final Throwable cause) {
        super(getMessage(url, parameters) + ", cannot parse response", cause);
    }
}
