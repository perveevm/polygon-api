package ru.perveevm.polygon.http;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import ru.perveevm.polygon.api.PolygonSession;

/**
 * Builder that is used to configure {@link PolygonHttpClient} instance.
 *
 * @author Mike Perveev (perveev_m@mail.ru)
 */
public class PolygonHttpClientBuilder {
    private PolygonHttpClient client;

    private PolygonHttpClientBuilder(final String login, final String password) {
        this.client = new PolygonHttpClient(login, password);
    }

    /**
     * Sets login and password for access to Polygon.
     *
     * @param login    Polygon user login.
     * @param password Polygon user password.
     * @return {@link PolygonHttpClientBuilder} with configured login and password.
     */
    public static PolygonHttpClientBuilder withCredentials(final String login, final String password) {
        return new PolygonHttpClientBuilder(login, password);
    }

    /**
     * Builds {@link PolygonHttpClient} with default parameters.
     *
     * @param login    Polygon user login.
     * @param password Polygon user password.
     * @return Initialized {@link PolygonHttpClient} instance.
     */
    public static PolygonHttpClient defaultPolygonHttpClient(final String login, final String password) {
        return withCredentials(login, password).build();
    }

    /**
     * Builds {@link PolygonHttpClient} with parameters that were set before.
     *
     * @return Initialized {@link PolygonHttpClient} instance.
     */
    public PolygonHttpClient build() {
        return client;
    }

    /**
     * Sets the HTTP client that will be used to perform requests to the API. The default value is created using
     * {@link HttpClients#createDefault()}
     *
     * @param client The instance of {@link CloseableHttpClient} interface.
     * @return Modified instance of {@link PolygonHttpClientBuilder}.
     */
    public PolygonHttpClientBuilder withClient(final CloseableHttpClient client) {
        this.client.setClient(client);
        return this;
    }

    /**
     * Sets the base URL to Polygon. The default value is
     * <a href="https://polygon.codeforces.com/">https://polygon.codeforces.com/</a>.
     *
     * @param baseUrl The URL to Polygon.
     * @return Modified instance of {@link PolygonHttpClientBuilder}.
     */
    public PolygonHttpClientBuilder withBaseUrl(final String baseUrl) {
        this.client.setBaseUrl(baseUrl);
        return this;
    }
}
