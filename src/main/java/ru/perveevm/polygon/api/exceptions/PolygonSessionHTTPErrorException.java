package ru.perveevm.polygon.api.exceptions;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
 * <p>
 * Thrown when a HTTP error occured while sendings POST request.
 */
public class PolygonSessionHTTPErrorException extends PolygonSessionException {
    /**
     * @param url        Request URL.
     * @param parameters A {@link List} of parameters.
     */
    public PolygonSessionHTTPErrorException(final String url, final List<NameValuePair> parameters) {
        super(getMessage(url, parameters));
    }

    /**
     * @param url        Request URL.
     * @param parameters A {@link List} of parameters.
     * @param cause      Cause of this exception.
     */
    public PolygonSessionHTTPErrorException(final String url, final List<NameValuePair> parameters,
                                            final Throwable cause) {
        super(getMessage(url, parameters), cause);
    }

    public PolygonSessionHTTPErrorException(final String url, final Throwable cause) {
        super(getMessage(url), cause);
    }
}
