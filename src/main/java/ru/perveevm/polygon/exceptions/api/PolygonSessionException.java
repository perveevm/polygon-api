package ru.perveevm.polygon.exceptions.api;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
 * <p>
 * Thrown if some error occures while perform API calls.
 */
public class PolygonSessionException extends Exception {
    /**
     * @param message Error message.
     */
    public PolygonSessionException(final String message) {
        super(message);
    }

    /**
     * @param message Error message.
     * @param cause   Exception that caused an error.
     */
    public PolygonSessionException(final String message, final Throwable cause) {
        super(message, cause);
    }

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

    protected static String getMessage(final String url) {
        return String.format("Error happened while performing POST request to %s", url);
    }
}
