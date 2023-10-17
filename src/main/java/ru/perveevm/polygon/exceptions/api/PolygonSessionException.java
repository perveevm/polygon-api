package ru.perveevm.polygon.exceptions.api;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Thrown if some error occurs while perform API calls.
 *
 * @author Perveev Mike (perveev_m@mail.ru)
 */
public class PolygonSessionException extends Exception {
    /**
     * Constructs {@link PolygonSessionException} using error message.
     *
     * @param message Error message.
     */
    public PolygonSessionException(final String message) {
        super(message);
    }

    /**
     * Constructs {@link PolygonSessionException} using error message and cause exception.
     *
     * @param message Error message.
     * @param cause   Exception that caused an error.
     */
    public PolygonSessionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Generates a human-readable error message that contains request URL and parameters.
     * API key and secret are hidden for security reasons.
     *
     * @param url        Request URL.
     * @param parameters Request parameters.
     * @return A {@link String} with a human-readable error message.
     */
    protected static String getMessage(final String url, final List<NameValuePair> parameters) {
        return String.format("Error happened while performing POST request to %s with parameters %s", url,
                parameters.stream()
                        .map(param -> {
                            if (param.getName().equals("apiKey") || param.getName().equals("apiSig")) {
                                return new BasicNameValuePair(param.getName(), "Hidden for security");
                            } else {
                                return param;
                            }
                        })
                        .map(param -> String.format("\"%s\": \"%s\"", param.getName(), param.getValue()))
                        .collect(Collectors.joining(", ", "{", "}")));
    }

    /**
     * Generates a human-readable error message that contains request URL.
     *
     * @param url Request URL.
     * @return A {@link String} with a human-readable error message.
     */
    protected static String getMessage(final String url) {
        return String.format("Error happened while performing POST request to %s", url);
    }
}
