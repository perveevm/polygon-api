package ru.perveevm.polygon.utils;

import me.tongfei.progressbar.ProgressBar;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import ru.perveevm.polygon.exceptions.api.PolygonSessionException;
import ru.perveevm.polygon.exceptions.api.PolygonSessionHTTPErrorException;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpUtils {
    public static HttpResponse sendGetRequest(final HttpClient client, final String url,
                                              final List<NameValuePair> parameters)
            throws IOException, URISyntaxException {
        HttpGet request = new HttpGet(url);
        URI uri = new URIBuilder(request.getURI()).addParameters(parameters).build();
        request.setURI(uri);
        return client.execute(request);
    }

    public static HttpResponse sendPostRequest(final HttpClient client, final String url,
                                               final List<NameValuePair> parameters) throws IOException {
        HttpPost request = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        for (NameValuePair p : parameters) {
            builder.addBinaryBody(p.getName(), p.getValue().getBytes(StandardCharsets.UTF_8));
        }
        HttpEntity entity = builder.build();
        request.setEntity(entity);
        return client.execute(request);
    }

    public static void downloadFile(final String url, final HttpResponse response, final File downloadPath)
            throws PolygonSessionException {
        try (ProgressBar pb = new ProgressBar("Downloading file", response.getEntity().getContentLength())) {
            try (BufferedInputStream inputStream = new BufferedInputStream(response.getEntity().getContent(), 1 << 20)) {
                try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadPath), 1 << 20)) {
                    int curByte;
                    while ((curByte = inputStream.read()) != -1) {
                        outputStream.write(curByte);
                        pb.step();
                    }
                } catch (IOException e) {
                    throw new PolygonSessionException("Cannot write file: " + e.getMessage(), e);
                }
            } catch (IOException e) {
                throw new PolygonSessionHTTPErrorException(url, e);
            }
        }
    }
}
