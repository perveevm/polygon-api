package ru.perveevm.polygon.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import lombok.NonNull;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import ru.perveevm.polygon.api.entities.*;
import ru.perveevm.polygon.api.entities.enums.*;
import ru.perveevm.polygon.api.exceptions.PolygonSessionBadResponseException;
import ru.perveevm.polygon.api.exceptions.PolygonSessionException;
import ru.perveevm.polygon.api.exceptions.PolygonSessionFailedRequestException;
import ru.perveevm.polygon.api.exceptions.PolygonSessionHTTPErrorException;
import ru.perveevm.polygon.api.json.JSONResponse;
import ru.perveevm.polygon.api.json.JSONResponseStatus;
import ru.perveevm.polygon.api.utils.ReflectionUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
 * <p>
 * Basic class for performing Polygon API calls.
 */
public class PolygonSession implements Closeable {
    private final static String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private final static String BASE_URL = "https://polygon.codeforces.com/api/";

    private final String key;
    private final String secret;

    private final Random random = new Random();
    private final CloseableHttpClient client = HttpClients.createDefault();
    private final Gson gson = new Gson();

    // TODO pin

    /**
     * Initializes Polygon session with API key.
     *
     * @param key    API key from Polygon.
     * @param secret API secret parameter from Polygon.
     */
    public PolygonSession(final String key, final String secret) {
        this.key = key;
        this.secret = secret;
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    public Problem[] problemsList(final Boolean showDeleted, final Integer id, final String name, final String owner)
            throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemsList", "problems.list", showDeleted, id, name, owner),
                Problem[].class);
    }

    public ProblemInfo problemInfo(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemInfo", "problem.info", problemId), ProblemInfo.class);
    }

    public void problemUpdateInfo(@NonNull final Integer problemId, final String inputFile, final String outputFile,
                                  final Boolean interactive, final Integer timeLimit, final Integer memoryLimit)
            throws PolygonSessionException {
        sendAPIRequest("problemUpdateInfo", "problem.updateInfo", problemId, inputFile, outputFile, interactive,
                timeLimit, memoryLimit);
    }

    public Map<String, Statement> problemStatements(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemStatements", "problem.statements", problemId),
                new TypeToken<Map<String, Statement>>() {
                }.getType());
    }

    public void problemSaveStatement(@NonNull final Integer problemId, @NonNull final String lang,
                                     final String encoding, final String name, final String legend, final String input,
                                     final String output, final String scoring, final String notes,
                                     final String tutorial) throws PolygonSessionException {
        sendAPIRequest("problemSaveStatement", "problem.saveStatement", problemId, lang, encoding, name, legend, input,
                output, scoring, notes, tutorial);
    }

    public ProblemFile[] problemStatementResources(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemStatementResources", "problem.statementResources", problemId),
                ProblemFile[].class);
    }

    public void problemSaveStatementResource(@NonNull final Integer problemId, final Boolean checkExisting,
                                             @NonNull final String name, @NonNull final String file)
            throws PolygonSessionException {
        sendAPIRequest("problemSaveStatementResource", "problem.saveStatementResource", problemId, checkExisting,
                name, file);
    }

    public void problemSaveStatementResource(@NonNull final Integer problemId, final Boolean checkExisting,
                                             @NonNull final String name, @NonNull final File file)
            throws PolygonSessionException {
        sendAPIRequest("problemSaveStatementResource", "problem.saveStatementResource", problemId, checkExisting,
                name, file);
    }

    public String problemChecker(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemChecker", "problem.checker", problemId), String.class);
    }

    public String problemValidator(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemValidator", "problem.validator", problemId), String.class);
    }

    public String problemInteractor(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemInteractor", "problem.interactor", problemId), String.class);
    }

    public ProblemFiles problemFiles(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemFiles", "problem.files", problemId), ProblemFiles.class);
    }

    public Solution[] problemSolutions(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemSolutions", "problem.solutions", problemId), Solution[].class);
    }

    public String problemViewFile(@NonNull final Integer problemId, @NonNull final String type,
                                  @NonNull final String name) throws PolygonSessionException {
        String result = sendAPIRequestPlain("problemViewFile", "problem.viewFile", problemId, type, name);
        try {
            JSONResponse response = gson.fromJson(result, JSONResponse.class);
            throw new PolygonSessionFailedRequestException(BASE_URL + "problem.viewFile", response.getComment());
        } catch (JsonSyntaxException | NullPointerException ignored) {
            return result;
        }
    }

    public String problemViewSolution(@NonNull final Integer problemId, @NonNull final String name)
            throws PolygonSessionException {
        String result = sendAPIRequestPlain("problemViewSolution", "problem.viewSolution", problemId, name);
        try {
            JSONResponse response = gson.fromJson(result, JSONResponse.class);
            throw new PolygonSessionFailedRequestException(BASE_URL + "problem.viewSolution", response.getComment());
        } catch (JsonSyntaxException | NullPointerException ignored) {
            return result;
        }
    }

    public String problemScript(@NonNull final Integer problemId, @NonNull final String testset)
            throws PolygonSessionException {
        String result = sendAPIRequestPlain("problemScript", "problem.script", problemId, testset);
        try {
            JSONResponse response = gson.fromJson(result, JSONResponse.class);
            throw new PolygonSessionFailedRequestException(BASE_URL + "problem.script", response.getComment());
        } catch (JsonSyntaxException | NullPointerException ignored) {
            return result;
        }
    }

    public ProblemTest[] problemTests(@NonNull final Integer problemId, @NonNull final String testset)
            throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemTests", "problem.tests", problemId, testset), ProblemTest[].class);
    }

    public String problemTestInput(@NonNull final Integer problemId, @NonNull final String testset,
                                   @NonNull final Integer testIndex) throws PolygonSessionException {
        String result = sendAPIRequestPlain("problemTestInput", "problem.testInput", problemId, testset, testIndex);
        try {
            JSONResponse response = gson.fromJson(result, JSONResponse.class);
            throw new PolygonSessionFailedRequestException(BASE_URL + "problem.testInput", response.getComment());
        } catch (JsonSyntaxException | NullPointerException ignored) {
            return result;
        }
    }

    public String problemTestAnswer(@NonNull final Integer problemId, @NonNull final String testset,
                                    @NonNull final Integer testIndex) throws PolygonSessionException {
        String result = sendAPIRequestPlain("problemTestAnswer", "problem.testAnswer", problemId, testset, testIndex);
        try {
            JSONResponse response = gson.fromJson(result, JSONResponse.class);
            throw new PolygonSessionFailedRequestException(BASE_URL + "problem.testAnswer", response.getComment());
        } catch (JsonSyntaxException | NullPointerException ignored) {
            return result;
        }
    }

    public void problemSetValidator(@NonNull final Integer problemId, @NonNull final String validator)
            throws PolygonSessionException {
        sendAPIRequest("problemSetValidator", "problem.setValidator", problemId, validator);
    }

    public void problemSetChecker(@NonNull final Integer problemId, @NonNull final String checker)
            throws PolygonSessionException {
        sendAPIRequest("problemSetChecker", "problem.setChecker", problemId, checker);
    }

    public void problemSetInteractor(@NonNull final Integer problemId, @NonNull final String interactor)
            throws PolygonSessionException {
        sendAPIRequest("problemSetInteractor", "problem.setInteractor", problemId, interactor);
    }

    public void problemSaveFile(@NonNull final Integer problemId, final Boolean checkExisting,
                                @NonNull final String type, @NonNull final String name, @NonNull final String file,
                                final String sourceType, final String[] forTypes, final ResourceStage[] stages,
                                final ResourceAsset[] assets) throws PolygonSessionException {
        sendAPIRequest("problemSaveFile", "problem.saveFile", problemId, checkExisting, type, name, file, sourceType,
                encodeArray(forTypes), encodeArray(stages), encodeArray(assets));
    }

    public void problemSaveFile(@NonNull final Integer problemId, final Boolean checkExisting,
                                @NonNull final String type, @NonNull final String name, @NonNull final File file,
                                final String sourceType, final String[] forTypes, final ResourceStage[] stages,
                                final ResourceAsset[] assets) throws PolygonSessionException {
        sendAPIRequest("problemSaveFile", "problem.saveFile", problemId, checkExisting, type, name, file, sourceType,
                encodeArray(forTypes), encodeArray(stages), encodeArray(assets));
    }

    public void problemSaveSolution(@NonNull final Integer problemId, final Boolean checkExisting,
                                    @NonNull final String name, @NonNull final String file, final String sourceType,
                                    final SolutionTag tag) throws PolygonSessionException {
        sendAPIRequest("problemSaveSolution", "problem.saveSolution", problemId, checkExisting, name, file, sourceType,
                tag);
    }

    public void problemSaveSolution(@NonNull final Integer problemId, final Boolean checkExisting,
                                    @NonNull final String name, @NonNull final File file, final String sourceType,
                                    final SolutionTag tag) throws PolygonSessionException {
        sendAPIRequest("problemSaveSolution", "problem.saveSolution", problemId, checkExisting, name, file, sourceType,
                tag);
    }

    public void problemEditSolutionExtraTags(@NonNull final Integer problemId, @NonNull final Boolean remove,
                                             @NonNull final String name, final String testset, final String testGroup,
                                             final SolutionTag tag) throws PolygonSessionException {
        sendAPIRequest("problemEditSolutionExtraTags", "problem.editSolutionExtraTags", problemId, remove, name,
                testset, testGroup, tag);
    }

    public void problemSaveScript(@NonNull final Integer problemId, @NonNull final String testset,
                                  @NonNull final String source) throws PolygonSessionException {
        sendAPIRequest("problemSaveScript", "problem.saveScript", problemId, testset, source);
    }

    public void problemSaveScript(@NonNull final Integer problemId, @NonNull final String testset,
                                  @NonNull final File source) throws PolygonSessionException {
        sendAPIRequest("problemSaveScript", "problem.saveScript", problemId, testset, source);
    }

    public void problemSaveTest(@NonNull final Integer problemId, final Boolean checkExisting,
                                @NonNull final String testset, @NonNull final Integer testIndex,
                                @NonNull final String testInput, final String testGroup, final Double testPoints,
                                final String testDescription, final Boolean testUseInStatements,
                                final String testInputForStatements, final String testOutputForStatements,
                                final Boolean verifyInputOutputForStatements) throws PolygonSessionException {
        sendAPIRequest("problemSaveTest", "problem.saveTest", problemId, checkExisting, testset, testIndex, testInput,
                testGroup, testPoints, testDescription, testUseInStatements, testInputForStatements,
                testOutputForStatements, verifyInputOutputForStatements);
    }

    public void problemSaveTest(@NonNull final Integer problemId, final Boolean checkExisting,
                                @NonNull final String testset, @NonNull final Integer testIndex,
                                @NonNull final File testInput, final String testGroup, final Double testPoints,
                                final String testDescription, final Boolean testUseInStatements,
                                final String testInputForStatements, final String testOutputForStatements,
                                final Boolean verifyInputOutputForStatements) throws PolygonSessionException {
        sendAPIRequest("problemSaveTest", "problem.saveTest", problemId, checkExisting, testset, testIndex, testInput,
                testGroup, testPoints, testDescription, testUseInStatements, testInputForStatements,
                testOutputForStatements, verifyInputOutputForStatements);
    }

    public void problemSetTestGroup(@NonNull final Integer problemId, @NonNull final String testset,
                                    @NonNull final String testGroup, final Integer testIndex,
                                    final Integer[] testIndices) throws PolygonSessionException {
        sendAPIRequest("problemSetTestGroup", "problem.setTestGroup", problemId, testset, testGroup, testIndex,
                encodeArray(testIndices));
    }

    public void problemEnableGroups(@NonNull final Integer problemId, @NonNull final String testset,
                                    @NonNull final Boolean enable) throws PolygonSessionException {
        sendAPIRequest("problemEnableGroups", "problem.enableGroups", problemId, testset, enable);
    }

    public void problemEnablePoints(@NonNull final Integer problemId, @NonNull final Boolean enable)
            throws PolygonSessionException {
        sendAPIRequest("problemEnablePoints", "problem.enablePoints", problemId, enable);
    }

    public TestGroup[] problemViewTestGroup(@NonNull final Integer problemId, @NonNull final String testset,
                                            final String group) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemViewTestGroup", "problem.viewTestGroup", problemId, testset,
                group), TestGroup[].class);
    }

    public void problemSaveTestGroup(@NonNull final Integer problemId, @NonNull final String testset,
                                     @NonNull final String group, final TestGroupPointsPolicy pointsPolicy,
                                     final TestGroupFeedbackPolicy feedbackPolicy, final String[] dependencies)
            throws PolygonSessionException {
        sendAPIRequest("problemSaveTestGroup", "problem.saveTestGroup", problemId, testset, group, pointsPolicy,
                feedbackPolicy, encodeArray(dependencies));
    }

    public String[] problemViewTags(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemViewTags", "problem.viewTags", problemId), String[].class);
    }

    public void problemSaveTags(@NonNull final Integer problemId, @NonNull final String[] tags)
            throws PolygonSessionException {
        sendAPIRequest("problemSaveTags", "problem.saveTags", problemId, encodeArray(tags));
    }

    public String problemViewGeneralDescription(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemViewGeneralDescription", "problem.viewGeneralDescription",
                problemId), String.class);
    }

    public void problemSaveGeneralDescription(@NonNull final Integer problemId, @NonNull final String description)
            throws PolygonSessionException {
        sendAPIRequest("problemSaveGeneralDescription", "problem.saveGeneralDescription", problemId, description);
    }

    public String problemViewGeneralTutorial(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemViewGeneralTutorial", "problem.viewGeneralTutorial", problemId),
                String.class);
    }

    public void problemSaveGeneralTutorial(@NonNull final Integer problemId, @NonNull final String tutorial)
            throws PolygonSessionException {
        sendAPIRequest("problemSaveGeneralTutorial", "problem.saveGeneralTutorial", problemId, tutorial);
    }

    public ProblemPackage[] problemPackages(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemPackages", "problem.packages", problemId), ProblemPackage[].class);
    }

    public void problemPackage(@NonNull final Integer problemId, @NonNull final Integer packageId,
                               @NonNull final File downloadPath)
            throws PolygonSessionException {
        String stringResponse = sendAPIRequestPlain("problemPackage", "problem.package", problemId, packageId);
        try {
            JSONResponse json = gson.fromJson(stringResponse, JSONResponse.class);
            throw new PolygonSessionFailedRequestException(BASE_URL + "problem.package", json.getComment());
        } catch (JsonSyntaxException | NullPointerException ignored) {
        }

        HttpResponse response = getAPIResponse("problemPackage", "problem.package", problemId, packageId);

        try (BufferedInputStream inputStream = new BufferedInputStream(response.getEntity().getContent())) {
            try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadPath))) {
                int curByte;
                while ((curByte = inputStream.read()) != -1) {
                    outputStream.write(curByte);
                }
            } catch (IOException e) {
                throw new PolygonSessionException("Cannot write package file: " + e.getMessage(), e);
            }
        } catch (IOException e) {
            throw new PolygonSessionHTTPErrorException(BASE_URL + "problem.package", e);
        }
    }

    public Map<String, Problem> contestProblems(@NonNull final Integer contestId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("contestProblems", "contest.problems", contestId),
                new TypeToken<Map<String, Problem>>() {
                }.getType());
    }

    private String encodeArray(final Object[] data) {
        if (data == null) {
            return null;
        }
        return Arrays.stream(data).map(Object::toString).collect(Collectors.joining(","));
    }

    private HttpResponse getAPIResponse(final String method, final String methodName, final Object... values)
            throws PolygonSessionException {
        List<NameValuePair> parameters = ReflectionUtils.encodeMethodParameters(
                ReflectionUtils.getMethodByName(this.getClass(), method), values);
        return getAPIResponse(methodName, parameters);
    }

    private String sendAPIRequestPlain(final String method, final String methodName, final Object... values)
            throws PolygonSessionException {
        List<NameValuePair> parameters = ReflectionUtils.encodeMethodParameters(
                ReflectionUtils.getMethodByName(this.getClass(), method), values);
        return sendAPIRequestPlain(methodName, parameters);
    }

    private JsonElement sendAPIRequest(final String method, final String methodName, final Object... values)
            throws PolygonSessionException {
        List<NameValuePair> parameters = ReflectionUtils.encodeMethodParameters(
                ReflectionUtils.getMethodByName(this.getClass(), method), values);
        return sendAPIRequest(methodName, parameters);
    }

    private HttpResponse getAPIResponse(final String methodName, final List<NameValuePair> parameters)
            throws PolygonSessionException {
        List<NameValuePair> extendedParameters = new ArrayList<>(parameters);
        extendedParameters.add(new BasicNameValuePair("apiKey", key));
        extendedParameters.add(new BasicNameValuePair("time", String.valueOf(System.currentTimeMillis() / 1000)));
        extendedParameters.add(new BasicNameValuePair("apiSig", generateApiSig(methodName, extendedParameters)));

        HttpResponse response;
        try {
            response = sendPostRequest(BASE_URL + methodName, extendedParameters);
        } catch (IOException e) {
            throw new PolygonSessionHTTPErrorException(BASE_URL + methodName, parameters, e);
        }

        return response;
    }

    private String sendAPIRequestPlain(final String methodName, final List<NameValuePair> parameters)
            throws PolygonSessionException {
        HttpResponse response = getAPIResponse(methodName, parameters);

        String responseText;
        try {
            responseText = EntityUtils.toString(response.getEntity());
        } catch (IOException | ParseException e) {
            throw new PolygonSessionBadResponseException(BASE_URL + methodName, parameters,
                    response.getStatusLine().getStatusCode(), e);
        }

        return responseText;
    }

    private JsonElement sendAPIRequest(final String methodName, final List<NameValuePair> parameters)
            throws PolygonSessionException {
        String json = sendAPIRequestPlain(methodName, parameters);

        JSONResponse jsonResponse = gson.fromJson(json, JSONResponse.class);
        if (jsonResponse.getStatus() == JSONResponseStatus.FAILED) {
            throw new PolygonSessionFailedRequestException(BASE_URL + methodName, parameters,
                    jsonResponse.getComment());
        }

        return jsonResponse.getResult();
    }

    private HttpResponse sendPostRequest(final String url, final List<NameValuePair> parameters)
            throws IOException {
        HttpPost request = new HttpPost(url);
        request.setEntity(new UrlEncodedFormEntity(parameters, StandardCharsets.UTF_8));
        return client.execute(request);
    }

    private String generateApiSig(final String methodName, final List<NameValuePair> parameters) {
        StringBuilder rand = new StringBuilder();
        StringBuilder apiSig = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            rand.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }

        apiSig.append(rand).append('/').append(methodName).append('?').append(parameters.stream()
                .sorted(Comparator.comparing(NameValuePair::getName).thenComparing(NameValuePair::getValue))
                .map(p -> p.getName() + "=" + p.getValue())
                .collect(Collectors.joining("&"))).append('#').append(secret);

        rand.append(DigestUtils.sha512Hex(apiSig.toString()));
        return rand.toString();
    }
}
