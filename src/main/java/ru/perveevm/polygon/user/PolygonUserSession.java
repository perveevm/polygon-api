package ru.perveevm.polygon.user;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.perveevm.polygon.exceptions.user.PolygonUserSessionException;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Basic class for performing Polygon user requests.
 *
 * @author Perveev Mike (perveev_m@mail.ru)
 */
@SuppressWarnings("unused")
public class PolygonUserSession implements Closeable {
    private String BASE_URL = "https://polygon.codeforces.com/";

    private final String login;
    private final String password;

    private final CloseableHttpClient client = HttpClients.createDefault();

    private String ccid = null;

    /**
     * Initializes Polygon user session using provided login and password
     *
     * @param login    Polygon user login
     * @param password Polygon user password
     */
    public PolygonUserSession(final String login, final String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * Initializes Polygon user session using provided login, password ans Polygon URL
     *
     * @param login    Polygon user login
     * @param password Polygon user password
     * @param baseUrl  Polygon URL, for example <a href="https://polygon.codeforces.com">https://polygon.codeforces.com/</a>
     */
    public PolygonUserSession(final String login, final String password, final String baseUrl) {
        this.login = login;
        this.password = password;
        this.BASE_URL = baseUrl;
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    /**
     * Authenticates user in Polygon using given login and password
     *
     * @throws PolygonUserSessionException if HTTP error happened while performing request
     */
    public void authorize() throws PolygonUserSessionException {
        if (isAuthorized()) {
            return;
        }

        Document html = parseDocument(getHtml(BASE_URL + "login"));
        Element form = html.getElementsByClass("enterForm").get(0);
        Optional<Element> ccidInput = form.getElementsByTag("input").stream()
                .filter(e -> e.hasAttr("name") && e.attr("name").equals("ccid"))
                .findFirst();

        if (ccidInput.isEmpty()) {
            throw new PolygonUserSessionException("There is no ccid parameter on login page");
        }

        ccid = ccidInput.get().attr("value");
        List<NameValuePair> parameters = List.of(
                new BasicNameValuePair("attachSessionToIp", "on"),
                new BasicNameValuePair("ccid", ccidInput.get().attr("value")),
                new BasicNameValuePair("fp", ""),
                new BasicNameValuePair("login", login),
                new BasicNameValuePair("password", password),
                new BasicNameValuePair("submit", "Login"),
                new BasicNameValuePair("submitted", "true")
        );

        sendPost(BASE_URL + "login", parameters);
    }

    /**
     * Checks if user is authenticated
     *
     * @return {@code true} if user is authenticated and {@code false} otherwise
     * @throws PolygonUserSessionException if HTTP error happened while performing request
     */
    public boolean isAuthorized() throws PolygonUserSessionException {
        return parseDocument(getHtml(BASE_URL)).getElementsByTag("a").stream().anyMatch(e -> e.text().equals("Logout"));
    }

    /**
     * Creates new problem in Polygon
     *
     * @param name created problem name
     * @throws PolygonUserSessionException if there already exists a problem with such name
     */
    @Deprecated(since = "1.2", forRemoval = true)
    public void problemCreate(final String name) throws PolygonUserSessionException {
        authorize();
        List<NameValuePair> parameters = List.of(
                new BasicNameValuePair("ccid", ccid),
                new BasicNameValuePair("name", name),
                new BasicNameValuePair("submit", "Create"),
                new BasicNameValuePair("submitted", "true")
        );

        HttpResponse response = sendPost(BASE_URL + "cp?ccid=" + ccid, parameters);
        if (response.getStatusLine().getStatusCode() == 200) {
            throw new PolygonUserSessionException("There exists problem with name: " + name);
        }
    }

    /**
     * Commits all changes at problem with given ID
     *
     * @param id problem ID in Polygon
     * @throws PolygonUserSessionException if HTTP error happened while performing request
     */
    @Deprecated(since = "1.2", forRemoval = true)
    public void problemCommit(final int id) throws PolygonUserSessionException {
        problemCommit(id, null);
    }

    /**
     * Commits all changes at problem with given name
     *
     * @param name problem name in Polygon
     * @throws PolygonUserSessionException if HTTP error happened while performing request
     */
    @Deprecated(since = "1.2", forRemoval = true)
    public void problemCommit(final String name) throws PolygonUserSessionException {
        problemCommit(null, name);
    }

    /**
     * Deletes problem with given ID
     *
     * @param id problem ID in Polygon
     * @throws PolygonUserSessionException if HTTP error happened while performing request
     */
    public void problemDelete(final int id) throws PolygonUserSessionException {
        String session = getSessionBySearchRequest(id, null);
        List<NameValuePair> parameters = List.of(
                new BasicNameValuePair("Delete", "Delete"),
                new BasicNameValuePair("action", "delete"),
                new BasicNameValuePair("ccid", ccid),
                new BasicNameValuePair("problemId", String.valueOf(id)),
                new BasicNameValuePair("session", session)
        );
        sendPost(BASE_URL + "deleteProblem", parameters);
    }

    /**
     * Builds package for problem with given ID
     *
     * @param id             problem ID in Polygon
     * @param createFull     if full package is required
     * @param doVerification if verification is required
     * @throws PolygonUserSessionException if HTTP error happened while performing request
     */
    @Deprecated(since = "1.2", forRemoval = true)
    public void problemBuildPackage(final int id, final boolean createFull, final boolean doVerification)
            throws PolygonUserSessionException {
        problemBuildPackage(id, null, createFull, doVerification);
    }

    /**
     * Builds package for problem with given name
     *
     * @param name           problem name in Polygon
     * @param createFull     if full package is required
     * @param doVerification if verification is requireD
     * @throws PolygonUserSessionException if HTTP error happened while performing request
     */
    @Deprecated(since = "1.2", forRemoval = true)
    public void problemBuildPackage(final String name, final boolean createFull, final boolean doVerification)
            throws PolygonUserSessionException {
        problemBuildPackage(null, name, createFull, doVerification);
    }

    /**
     * Gets problem link with salt (used to import problem in Codeforces contest)
     *
     * @param id problem ID in Polygon
     * @return {@link String} with problem link
     * @throws PolygonUserSessionException if HTTP error happened while performing request
     */
    public String problemGetShareURL(final int id) throws PolygonUserSessionException {
        return problemGetShareUrl(id, null);
    }

    /**
     * Gets problem link with salt (used to import problem in Codeforces contest)
     *
     * @param name problem name in Polygon
     * @return {@link String} with problem link
     * @throws PolygonUserSessionException if HTTP error happened while performing request
     */
    public String problemGetShareUrl(final String name) throws PolygonUserSessionException {
        return problemGetShareUrl(null, name);
    }

    /**
     * Downloads russian PDF statements from package
     *
     * @param contestId    contest ID in Polygon
     * @param downloadPath {@link Path} where to save PDF statements
     * @throws PolygonUserSessionException if HTTP error happened while performing request
     */
    public void contestGetStatementsFromPackages(final int contestId, final Path downloadPath)
            throws PolygonUserSessionException {
        authorize();

        HttpGet request = new HttpGet(
                BASE_URL + "/contest/statements/contest-" + contestId + "-ru.pdf?contestId=" + contestId +
                        "&action=previewAsPDF&language=russian&fromWorkingCopies=false&ccid=" + ccid);
        try {
            HttpResponse response = client.execute(request);
            InputStream is = response.getEntity().getContent();
            Files.copy(is, downloadPath);
        } catch (IOException e) {
            throw new PolygonUserSessionException("Could not download statements", e);
        }
    }

    private String problemGetShareUrl(final Integer id, final String name) throws PolygonUserSessionException {
        authorize();

        String html = getProblemPage(id, name).toString();
        int pos = html.indexOf("supportCopyingToClipboard");
        int firstQuote = html.indexOf("\"", pos);
        int secondQuote = html.indexOf("\"", firstQuote + 1);
        int thirdQuote = html.indexOf("\"", secondQuote + 1);
        int fourthQuote = html.indexOf("\"", thirdQuote + 1);

        return html.substring(thirdQuote + 1, fourthQuote);
    }

    private void problemBuildPackage(final Integer id, final String name, final boolean createFull,
                                     final boolean doVerification) throws PolygonUserSessionException {
        String session = getSessionBySearchRequest(id, name);
        List<NameValuePair> parameters = List.of(
                new BasicNameValuePair("action", "create"),
                new BasicNameValuePair("ccid", ccid),
                new BasicNameValuePair("createFull", String.valueOf(createFull)),
                new BasicNameValuePair("doVerification", doVerification ? "on" : "off"),
                new BasicNameValuePair("session", session)
        );
        sendPost(BASE_URL + "package?ccid=" + ccid + "&session=" + session, parameters);
    }

    private void problemCommit(final Integer id, final String name) throws PolygonUserSessionException {
        String session = getSessionBySearchRequest(id, name);
        List<NameValuePair> parameters = List.of(
                new BasicNameValuePair("allContests", "true"),
                new BasicNameValuePair("ccid", ccid),
                new BasicNameValuePair("message", ""),
                new BasicNameValuePair("minorChanges", "on"),
                new BasicNameValuePair("session", session),
                new BasicNameValuePair("submitted", "true")
        );
        sendPost(BASE_URL + "edit-commit?ccid" + ccid + "&session=" + session, parameters);
    }

    private List<Element> searchProblemByIdOrName(final Integer id, final String name) throws PolygonUserSessionException {
        authorize();

        String request = (id == null) ? "name:" + name : "id:" + id;
        List<NameValuePair> parameters = List.of(
                new BasicNameValuePair("action", "search"),
                new BasicNameValuePair("ccid", ccid),
                new BasicNameValuePair("query", request),
                new BasicNameValuePair("search", "Search")
        );

        HttpResponse response = sendPost(BASE_URL + "search?ccid=" + ccid, parameters);
        String html;
        try {
            html = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new PolygonUserSessionException("Error happened while parsing response", e);
        }

        return parseDocument(html).getElementsByTag("tr").stream()
                .filter(e -> e.hasAttr("problemid") && e.hasAttr("problemname"))
                .filter(e -> {
                    if (id != null) {
                        return e.attr("problemid").equals(String.valueOf(id));
                    } else {
                        return e.attr("problemname").equals(name);
                    }
                })
                .collect(Collectors.toList());
    }

    private Element getProblemPage(final Integer id, final String name) throws PolygonUserSessionException {
        List<Element> rows = searchProblemByIdOrName(id, name);

        if (rows.isEmpty()) {
            throw new PolygonUserSessionException("There are no problems with given ID or name");
        }
        if (rows.size() > 1) {
            throw new PolygonUserSessionException("There are many problems with given ID or name");
        }

        Element row = rows.get(0);
        String link = null;
        if (!row.getElementsByClass("START_EDIT_SESSION").isEmpty()) {
            link = row.getElementsByClass("START_EDIT_SESSION").get(0).attr("href");
        }
        if (!row.getElementsByClass("CONTINUE_EDIT_SESSION").isEmpty()) {
            link = row.getElementsByClass("CONTINUE_EDIT_SESSION").get(0).attr("href");
        }

        if (link == null) {
            throw new PolygonUserSessionException("Problem not found");
        }

        return parseDocument(getHtml(BASE_URL + link));
    }

    private String getSessionBySearchRequest(final Integer id, final String name) throws PolygonUserSessionException {
        authorize();
        Element sessionTag = getProblemPage(id, name).getElementById("session");
        if (sessionTag == null) {
            throw new PolygonUserSessionException("Cannot find session");
        }

        return sessionTag.text();
    }

    private String getHtml(final String url) throws PolygonUserSessionException {
        try {
            return EntityUtils.toString(client.execute(new HttpGet(url)).getEntity());
        } catch (IOException e) {
            throw new PolygonUserSessionException("Error happened while performing GET request to " + url, e);
        }
    }

    private HttpResponse sendPost(final String url, final List<NameValuePair> parameters)
            throws PolygonUserSessionException {
        HttpPost request = new HttpPost(url);
        try {
            request.setEntity(new UrlEncodedFormEntity(parameters));
            return client.execute(request);
        } catch (IOException e) {
            throw new PolygonUserSessionException("Error happened while performing POST request to " + url, e);
        }
    }

    private Document parseDocument(final String html) {
        return Jsoup.parse(html);
    }
}
