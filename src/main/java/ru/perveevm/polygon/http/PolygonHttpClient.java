package ru.perveevm.polygon.http;

import lombok.NonNull;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import ru.perveevm.polygon.exceptions.api.PolygonSessionException;
import ru.perveevm.polygon.exceptions.api.PolygonSessionFailedRequestException;
import ru.perveevm.polygon.exceptions.api.PolygonSessionHTTPErrorException;
import ru.perveevm.polygon.utils.HttpUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains three methods for downloading problem package, problem descriptor and contest descriptor
 * from Polygon. All methods do not use Polygon API but use login based authorization.
 *
 * @author Mike Perveev (perveev_m@mail.ru)
 */
public class PolygonHttpClient {
    private final String login;
    private final String password;

    private CloseableHttpClient client = HttpClients.createDefault();
    private String baseUrl = "https://polygon.codeforces.com/";

    PolygonHttpClient(final String login, final String password) {
        this.login = login;
        this.password = password;
    }

    void setClient(final CloseableHttpClient client) {
        this.client = client;
    }

    void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void downloadProblemPackage(@NonNull final String problemUrl, final Integer revision, final String type,
                                       final String pin, @NonNull final File downloadPath)
            throws PolygonSessionException {
        List<NameValuePair> parameters = getLoginPasswordParameters();
        if (revision != null) {
            parameters.add(new BasicNameValuePair("revision", String.valueOf(revision)));
        }
        if (type != null) {
            parameters.add(new BasicNameValuePair("type", type));
        }
        if (pin != null) {
            parameters.add(new BasicNameValuePair("pin", pin));
        }

        HttpResponse response;
        try {
            response = HttpUtils.sendGetRequest(client, problemUrl, parameters);
        } catch (IOException e) {
            throw new PolygonSessionHTTPErrorException(problemUrl, parameters, e);
        } catch (URISyntaxException e) {
            throw new PolygonSessionFailedRequestException(problemUrl, parameters, "Bad problem URL", e);
        }

        HttpUtils.downloadFile(problemUrl, response, downloadPath);
    }

    public void downloadProblemDescriptor(@NonNull final String problemUrl, final Integer revision, final String pin,
                                          @NonNull final File downloadPath) throws PolygonSessionException {
        List<NameValuePair> parameters = getLoginPasswordParameters();
        if (revision != null) {
            parameters.add(new BasicNameValuePair("revision", String.valueOf(revision)));
        }
        if (pin != null) {
            parameters.add(new BasicNameValuePair("pin", pin));
        }

        String url = problemUrl + "/problem.xml";
        HttpResponse response;
        try {
            response = HttpUtils.sendGetRequest(client, url, parameters);
        } catch (IOException e) {
            throw new PolygonSessionHTTPErrorException(url, parameters, e);
        } catch (URISyntaxException e) {
            throw new PolygonSessionFailedRequestException(url, parameters, "Bad problem.xml URL", e);
        }

        HttpUtils.downloadFile(url, response, downloadPath);
    }

    public void downloadContestDescriptor(@NonNull final String contestUID, final String pin,
                                          @NonNull final File downloadPath) throws PolygonSessionException {
        List<NameValuePair> parameters = getLoginPasswordParameters();
        if (pin != null) {
            parameters.add(new BasicNameValuePair("pin", pin));
        }

        String url = baseUrl + "/c/" + contestUID + "/contest.xml";
        HttpResponse response;
        try {
            response = HttpUtils.sendGetRequest(client, url, parameters);
        } catch (IOException e) {
            throw new PolygonSessionHTTPErrorException(url, parameters, e);
        } catch (URISyntaxException e) {
            throw new PolygonSessionFailedRequestException(url, parameters, "Bad contest.xml URL", e);
        }

        HttpUtils.downloadFile(url, response, downloadPath);
    }

    private List<NameValuePair> getLoginPasswordParameters() {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("login", login));
        parameters.add(new BasicNameValuePair("password", password));
        return parameters;
    }
}
