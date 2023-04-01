package ru.perveevm.polygon.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import lombok.NonNull;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import ru.perveevm.polygon.api.entities.*;
import ru.perveevm.polygon.api.entities.enums.*;
import ru.perveevm.polygon.exceptions.api.PolygonSessionBadResponseException;
import ru.perveevm.polygon.exceptions.api.PolygonSessionException;
import ru.perveevm.polygon.exceptions.api.PolygonSessionFailedRequestException;
import ru.perveevm.polygon.exceptions.api.PolygonSessionHTTPErrorException;
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

    private String pin = null;

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

    /**
     * Sets <code>pin</code> that will be used in all following requests. You can set it to <code>null</code> if no pin
     * is required.
     *
     * @param pin Polygon pin code for problem or contest access.
     */
    public void setPin(final String pin) {
        this.pin = pin;
    }

    /**
     * Returns a list of problems, available to the user, according to search parameters.
     *
     * @param showDeleted Searches for deleted problems too if <code>true</code>. Can be <code>null</code>.
     * @param id          Searches problem by id, can be <code>null</code>.
     * @param name        Searches problem by name, can be <code>null</code>.
     * @param owner       Searches problem by owner, can be <code>null</code>.
     * @return Array of {@link Problem} objects.
     */
    public Problem[] problemsList(final Boolean showDeleted, final Integer id, final String name, final String owner)
            throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemsList", "problems.list", showDeleted, id, name, owner),
                Problem[].class);
    }

    /**
     * Returns problem general info.
     *
     * @param problemId Problem ID.
     * @return {@link ProblemInfo} object.
     */
    public ProblemInfo problemInfo(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemInfo", "problem.info", problemId), ProblemInfo.class);
    }

    /**
     * Update problem info. All parameters except <code>problemId</code> can be <code>null</code>.
     *
     * @param problemId   Problem ID.
     * @param inputFile   Input file name.
     * @param outputFile  Output file name.
     * @param interactive Is problem interactive.
     * @param timeLimit   Time limit in milliseconds.
     * @param memoryLimit Memory limit in megabytes.
     */
    public void problemUpdateInfo(@NonNull final Integer problemId, final String inputFile, final String outputFile,
                                  final Boolean interactive, final Integer timeLimit, final Integer memoryLimit)
            throws PolygonSessionException {
        sendAPIRequest("problemUpdateInfo", "problem.updateInfo", problemId, inputFile, outputFile, interactive,
                timeLimit, memoryLimit);
    }

    /**
     * Returns a {@link Map} from language to a {@link Statement} object for that language.
     *
     * @param problemId Problem ID.
     * @return A described {@link Map}.
     */
    public Map<String, Statement> problemStatements(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemStatements", "problem.statements", problemId),
                new TypeToken<Map<String, Statement>>() {
                }.getType());
    }

    /**
     * Update or create a problem’s statement. All parameters except for <code>lang</code> and <code>problemId</code>
     * can be <code>null</code>.
     *
     * @param problemId Problem ID.
     * @param lang      Problem language.
     * @param encoding  Statement encoding.
     * @param name      Problem name.
     * @param legend    Problem legend.
     * @param input     Problem input format.
     * @param output    Problem output format.
     * @param scoring   Problem scoring description.
     * @param notes     Problem notes.
     * @param tutorial  Problem tutorial.
     */
    public void problemSaveStatement(@NonNull final Integer problemId, @NonNull final String lang,
                                     final String encoding, final String name, final String legend, final String input,
                                     final String output, final String scoring, final String notes,
                                     final String tutorial) throws PolygonSessionException {
        sendAPIRequest("problemSaveStatement", "problem.saveStatement", problemId, lang, encoding, name, legend, input,
                output, scoring, notes, tutorial);
    }

    /**
     * Returns a list of statement resources for the problem.
     *
     * @param problemId Problem ID.
     * @return An array of {@link ProblemFile} objects representing resources.
     */
    public ProblemFile[] problemStatementResources(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemStatementResources", "problem.statementResources", problemId),
                ProblemFile[].class);
    }

    /**
     * Add or edit statement resource file.
     *
     * @param problemId     Problem ID.
     * @param checkExisting If <code>true</code>, only adding is allowed.
     * @param name          File name.
     * @param file          File content.
     */
    public void problemSaveStatementResource(@NonNull final Integer problemId, final Boolean checkExisting,
                                             @NonNull final String name, @NonNull final String file)
            throws PolygonSessionException {
        sendAPIRequest("problemSaveStatementResource", "problem.saveStatementResource", problemId, checkExisting,
                name, file);
    }

    /**
     * Add or edit statement resource file.
     *
     * @param problemId     Problem ID.
     * @param checkExisting If <code>true</code>, only adding is allowed.
     * @param name          File name.
     * @param file          File descriptor.
     */
    public void problemSaveStatementResource(@NonNull final Integer problemId, final Boolean checkExisting,
                                             @NonNull final String name, @NonNull final File file)
            throws PolygonSessionException {
        sendAPIRequest("problemSaveStatementResource", "problem.saveStatementResource", problemId, checkExisting,
                name, file);
    }

    /**
     * Returns the name of currently set checker.
     *
     * @param problemId Problem ID.
     * @return {@link String} object, contains checker name.
     */
    public String problemChecker(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemChecker", "problem.checker", problemId), String.class);
    }

    /**
     * Returns the name of currently set validator.
     *
     * @param problemId Problem ID.
     * @return {@link String} object, contains validator name.
     */
    public String problemValidator(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemValidator", "problem.validator", problemId), String.class);
    }

    /**
     * Returns the name of currently set interactor.
     *
     * @param problemId Problem ID.
     * @return {@link String} object, contains interactor name.
     */
    public String problemInteractor(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemInteractor", "problem.interactor", problemId), String.class);
    }

    /**
     * Returns the list of resource, source and aux files.
     *
     * @param problemId Problem ID.
     * @return {@link ProblemFiles} object.
     */
    public ProblemFiles problemFiles(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemFiles", "problem.files", problemId), ProblemFiles.class);
    }

    /**
     * Returns the list of problem solutions.
     *
     * @param problemId Problem ID.
     * @return An array of {@link Solution} objects.
     */
    public Solution[] problemSolutions(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemSolutions", "problem.solutions", problemId), Solution[].class);
    }

    /**
     * Returns resource, source or aux file.
     *
     * @param problemId Problem ID.
     * @param type      Resource type: <code>resource</code>, <code>source</code> or <code>aux</code>.
     * @param name      File name.
     * @return File content in a {@link String} object.
     */
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

    /**
     * Returns solution file.
     *
     * @param problemId Problem ID.
     * @param name      File name.
     * @return File content in a {@link String} object.
     */
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

    /**
     * Returns script for generating tests.
     *
     * @param problemId Problem ID.
     * @param testset   Testset name.
     * @return Script content in a {@link String} object.
     */
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

    /**
     * Returns tests for the given testset.
     *
     * @param problemId Problem ID.
     * @param testset   Testset name.
     * @return An array of {@link ProblemTest} objects.
     */
    public ProblemTest[] problemTests(@NonNull final Integer problemId, @NonNull final String testset)
            throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemTests", "problem.tests", problemId, testset), ProblemTest[].class);
    }

    /**
     * Returns generated test input.
     *
     * @param problemId Problem ID.
     * @param testset   Testset name.
     * @param testIndex Test index.
     * @return Test input content in a {@link String} object.
     */
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

    /**
     * Returns generated test answer.
     *
     * @param problemId Problem ID.
     * @param testset   Testset name.
     * @param testIndex Test index.
     * @return Test output content in a {@link String} object.
     */
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

    /**
     * Update validator.
     *
     * @param problemId Problem ID.
     * @param validator Validator file name.
     */
    public void problemSetValidator(@NonNull final Integer problemId, @NonNull final String validator)
            throws PolygonSessionException {
        sendAPIRequest("problemSetValidator", "problem.setValidator", problemId, validator);
    }

    /**
     * Update checker.
     *
     * @param problemId Problem ID.
     * @param checker   Checker file name.
     */
    public void problemSetChecker(@NonNull final Integer problemId, @NonNull final String checker)
            throws PolygonSessionException {
        sendAPIRequest("problemSetChecker", "problem.setChecker", problemId, checker);
    }

    /**
     * Update interactor.
     *
     * @param problemId  Problem ID.
     * @param interactor Interactor file name.
     */
    public void problemSetInteractor(@NonNull final Integer problemId, @NonNull final String interactor)
            throws PolygonSessionException {
        sendAPIRequest("problemSetInteractor", "problem.setInteractor", problemId, interactor);
    }

    /**
     * Add or edit resource, source or aux file. In case of editing, all parameters, except <code>problemId</code>,
     * <code>type</code> and <code>name</code> can be <code>null</code>.
     *
     * @param problemId     Problem ID.
     * @param checkExisting If <code>true</code>, only adding is allowed.
     * @param type          File type: <code>resource</code>, <code>source</code> or <code>aux</code>.
     * @param name          File name.
     * @param file          File content.
     * @param sourceType    Source type (only for source files).
     * @param forTypes      An array of file types it can be applied to (only for resource files).
     * @param stages        An array of stages for which it can be used to (only for resource files).
     * @param assets        An array of assets for which it can be user to (only for resource files).
     */
    public void problemSaveFile(@NonNull final Integer problemId, final Boolean checkExisting,
                                @NonNull final String type, @NonNull final String name, @NonNull final String file,
                                final String sourceType, final String[] forTypes, final ResourceStage[] stages,
                                final ResourceAsset[] assets) throws PolygonSessionException {
        sendAPIRequest("problemSaveFile", "problem.saveFile", problemId, checkExisting, type, name, file, sourceType,
                encodeArray(forTypes), encodeArray(stages), encodeArray(assets));
    }

    /**
     * Add or edit resource, source or aux file. In case of editing, all parameters, except <code>problemId</code>,
     * <code>type</code> and <code>name</code> can be <code>null</code>.
     *
     * @param problemId     Problem ID.
     * @param checkExisting If <code>true</code>, only adding is allowed.
     * @param type          File type: <code>resource</code>, <code>source</code> or <code>aux</code>.
     * @param name          File name.
     * @param file          File descriptor.
     * @param sourceType    Source type (only for source files).
     * @param forTypes      An array of file types it can be applied to (only for resource files).
     * @param stages        An array of stages for which it can be used to (only for resource files).
     * @param assets        An array of assets for which it can be user to (only for resource files).
     */
    public void problemSaveFile(@NonNull final Integer problemId, final Boolean checkExisting,
                                @NonNull final String type, @NonNull final String name, @NonNull final File file,
                                final String sourceType, final String[] forTypes, final ResourceStage[] stages,
                                final ResourceAsset[] assets) throws PolygonSessionException {
        sendAPIRequest("problemSaveFile", "problem.saveFile", problemId, checkExisting, type, name, file, sourceType,
                encodeArray(forTypes), encodeArray(stages), encodeArray(assets));
    }

    /**
     * Add or edit solution. In case of editing, all parameters except <code>problemId</code> and <code>name</code>
     * can be <code>null</code>.
     *
     * @param problemId     Problem ID.
     * @param checkExisting If <code>true</code>, only adding is allowed.
     * @param name          File name.
     * @param file          File content.
     * @param sourceType    File source type.
     * @param tag           Solution tag.
     */
    public void problemSaveSolution(@NonNull final Integer problemId, final Boolean checkExisting,
                                    @NonNull final String name, @NonNull final String file, final String sourceType,
                                    final SolutionTag tag) throws PolygonSessionException {
        sendAPIRequest("problemSaveSolution", "problem.saveSolution", problemId, checkExisting, name, file, sourceType,
                tag);
    }

    /**
     * Add or edit solution. In case of editing, all parameters except <code>problemId</code> and <code>name</code>
     * can be <code>null</code>.
     *
     * @param problemId     Problem ID.
     * @param checkExisting If <code>true</code>, only adding is allowed.
     * @param name          File name.
     * @param file          File descriptor.
     * @param sourceType    File source type.
     * @param tag           Solution tag.
     */
    public void problemSaveSolution(@NonNull final Integer problemId, final Boolean checkExisting,
                                    @NonNull final String name, @NonNull final File file, final String sourceType,
                                    final SolutionTag tag) throws PolygonSessionException {
        sendAPIRequest("problemSaveSolution", "problem.saveSolution", problemId, checkExisting, name, file, sourceType,
                tag);
    }

    /**
     * Add or remove testset or test group extra tag for solution.
     *
     * @param problemId Problem ID.
     * @param remove    If <code>true</code>, tag will be removed, or added otherwise.
     * @param name      Solution file name.
     * @param testset   Testset name. Can be <code>null</code>.
     * @param testGroup Group name. Can be <code>null</code>.
     * @param tag       Solution tag.
     */
    public void problemEditSolutionExtraTags(@NonNull final Integer problemId, @NonNull final Boolean remove,
                                             @NonNull final String name, final String testset, final String testGroup,
                                             final SolutionTag tag) throws PolygonSessionException {
        sendAPIRequest("problemEditSolutionExtraTags", "problem.editSolutionExtraTags", problemId, remove, name,
                testset, testGroup, tag);
    }

    /**
     * Edit script.
     *
     * @param problemId Problem ID.
     * @param testset   Testset name.
     * @param source    Script content.
     */
    public void problemSaveScript(@NonNull final Integer problemId, @NonNull final String testset,
                                  @NonNull final String source) throws PolygonSessionException {
        sendAPIRequest("problemSaveScript", "problem.saveScript", problemId, testset, source);
    }

    /**
     * Edit script.
     *
     * @param problemId Problem ID.
     * @param testset   Testset name.
     * @param source    Script file descriptor.
     */
    public void problemSaveScript(@NonNull final Integer problemId, @NonNull final String testset,
                                  @NonNull final File source) throws PolygonSessionException {
        sendAPIRequest("problemSaveScript", "problem.saveScript", problemId, testset, source);
    }

    /**
     * Add or edit test. In case of editing, all parameters except <code>problemId</code>, <code>testset</code>
     * and <code>testIndex</code> can be <code>null</code>.
     *
     * @param problemId                      Problem ID.
     * @param checkExisting                  If <code>true</code>, only adding is allowed.
     * @param testset                        Testset name.
     * @param testIndex                      Test index.
     * @param testInput                      Test input data.
     * @param testGroup                      Test group name.
     * @param testPoints                     Test points score.
     * @param testDescription                Test description.
     * @param testUseInStatements            If <code>true</code>, test will be used in statements.
     * @param testInputForStatements         Test input data for statements.
     * @param testOutputForStatements        Test output data for statements.
     * @param verifyInputOutputForStatements If <code>true</code>, input/output data for statements will be checked.
     */
    public void problemSaveTest(@NonNull final Integer problemId, final Boolean checkExisting,
                                @NonNull final String testset, @NonNull final Integer testIndex,
                                final String testInput, final String testGroup, final Double testPoints,
                                final String testDescription, final Boolean testUseInStatements,
                                final String testInputForStatements, final String testOutputForStatements,
                                final Boolean verifyInputOutputForStatements) throws PolygonSessionException {
        sendAPIRequest("problemSaveTest", "problem.saveTest", problemId, checkExisting, testset, testIndex, testInput,
                testGroup, testPoints, testDescription, testUseInStatements, testInputForStatements,
                testOutputForStatements, verifyInputOutputForStatements);
    }

    /**
     * Add or edit test. In case of editing, all parameters except <code>problemId</code>, <code>testset</code>
     * and <code>testIndex</code> can be <code>null</code>.
     *
     * @param problemId                      Problem ID.
     * @param checkExisting                  If <code>true</code>, only adding is allowed.
     * @param testset                        Testset name.
     * @param testIndex                      Test index.
     * @param testInput                      Test input file descriptor.
     * @param testGroup                      Test group name.
     * @param testPoints                     Test points score.
     * @param testDescription                Test description.
     * @param testUseInStatements            If <code>true</code>, test will be used in statements.
     * @param testInputForStatements         Test input data for statements.
     * @param testOutputForStatements        Test output data for statements.
     * @param verifyInputOutputForStatements If <code>true</code>, input/output data for statements will be checked.
     */
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

    /**
     * Set test group for one or more tests. It expects that for specified testset test groups are enabled.
     * Parameters <code>testIndex</code> and <code>testIndices</code> are mutually exclusive.
     *
     * @param problemId   Problem ID.
     * @param testset     Testset name.
     * @param testGroup   Group name.
     * @param testIndex   Test index to set group for.
     * @param testIndices Array of test indices.
     */
    public void problemSetTestGroup(@NonNull final Integer problemId, @NonNull final String testset,
                                    @NonNull final String testGroup, final Integer testIndex,
                                    final Integer[] testIndices) throws PolygonSessionException {
        sendAPIRequest("problemSetTestGroup", "problem.setTestGroup", problemId, testset, testGroup, testIndex,
                encodeArray(testIndices));
    }

    /**
     * Enable or disable test groups for the specified testset.
     *
     * @param problemId Problem ID.
     * @param testset   Testset name.
     * @param enable    If <code>true</code>, test groups will be enabled, or disabled otherwise.
     */
    public void problemEnableGroups(@NonNull final Integer problemId, @NonNull final String testset,
                                    @NonNull final Boolean enable) throws PolygonSessionException {
        sendAPIRequest("problemEnableGroups", "problem.enableGroups", problemId, testset, enable);
    }

    /**
     * Enable or disable test points for the problem.
     *
     * @param problemId Problem ID.
     * @param enable    If <code>true</code>, test points will be enabled, or disabled otherwise.
     */
    public void problemEnablePoints(@NonNull final Integer problemId, @NonNull final Boolean enable)
            throws PolygonSessionException {
        sendAPIRequest("problemEnablePoints", "problem.enablePoints", problemId, enable);
    }

    /**
     * Returns test groups for the specified testset.
     *
     * @param problemId Problem ID.
     * @param testset   Testset name.
     * @param group     Group name to be returned. Can be <code>null</code>.
     * @return An array of {@link TestGroup} objects.
     */
    public TestGroup[] problemViewTestGroup(@NonNull final Integer problemId, @NonNull final String testset,
                                            final String group) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemViewTestGroup", "problem.viewTestGroup", problemId, testset,
                group), TestGroup[].class);
    }

    /**
     * Saves test group. Use if only to save a test group.
     * If you want to create new test group, just add new test with such test group.
     *
     * @param problemId      Problem ID.
     * @param testset        Testset name.
     * @param group          Group name.
     * @param pointsPolicy   Points giving policy. Can be <code>null</code>.
     * @param feedbackPolicy Feedback policy. Can be <code>null</code>.
     * @param dependencies   Array of groups' names – dependencies groups. Can be <code>null</code>.
     */
    public void problemSaveTestGroup(@NonNull final Integer problemId, @NonNull final String testset,
                                     @NonNull final String group, final TestGroupPointsPolicy pointsPolicy,
                                     final TestGroupFeedbackPolicy feedbackPolicy, final String[] dependencies)
            throws PolygonSessionException {
        sendAPIRequest("problemSaveTestGroup", "problem.saveTestGroup", problemId, testset, group, pointsPolicy,
                feedbackPolicy, encodeArray(dependencies));
    }

    /**
     * Returns tags for the problem.
     *
     * @param problemId Problem ID.
     * @return Array of tags in {@link String} objects.
     */
    public String[] problemViewTags(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemViewTags", "problem.viewTags", problemId), String[].class);
    }

    /**
     * Saves tags for the problem. Existed tags will be replaced by new tags.
     *
     * @param problemId Problem ID.
     * @param tags      Array of new tags.
     */
    public void problemSaveTags(@NonNull final Integer problemId, @NonNull final String[] tags)
            throws PolygonSessionException {
        sendAPIRequest("problemSaveTags", "problem.saveTags", problemId, encodeArray(tags));
    }

    /**
     * Returns problem general description.
     *
     * @param problemId Problem ID.
     * @return Problem general description in a {@link String} object.
     */
    public String problemViewGeneralDescription(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemViewGeneralDescription", "problem.viewGeneralDescription",
                problemId), String.class);
    }

    /**
     * Saves problem general description.
     *
     * @param problemId   Problem ID.
     * @param description Problem general description.
     */
    public void problemSaveGeneralDescription(@NonNull final Integer problemId, @NonNull final String description)
            throws PolygonSessionException {
        sendAPIRequest("problemSaveGeneralDescription", "problem.saveGeneralDescription", problemId, description);
    }

    /**
     * Returns problem general tutorial.
     *
     * @param problemId Problem ID.
     * @return Problem general tutorial in a {@link String} object.
     */
    public String problemViewGeneralTutorial(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemViewGeneralTutorial", "problem.viewGeneralTutorial", problemId),
                String.class);
    }

    /**
     * Saves problem general tutorial.
     *
     * @param problemId Problem ID.
     * @param tutorial  Problem general tutorial.
     */
    public void problemSaveGeneralTutorial(@NonNull final Integer problemId, @NonNull final String tutorial)
            throws PolygonSessionException {
        sendAPIRequest("problemSaveGeneralTutorial", "problem.saveGeneralTutorial", problemId, tutorial);
    }

    /**
     * Returns list of all packages available for the problem.
     *
     * @param problemId Problem ID.
     * @return An array of {@link ProblemPackage} objects.
     */
    public ProblemPackage[] problemPackages(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemPackages", "problem.packages", problemId), ProblemPackage[].class);
    }

    /**
     * Download a package as a zip-archive.
     *
     * @param problemId    Problem ID.
     * @param packageId    Package ID.
     * @param downloadPath Download file descriptor.
     */
    public void problemPackage(@NonNull final Integer problemId, @NonNull final Integer packageId, final String type,
                               @NonNull final File downloadPath)
            throws PolygonSessionException {
        String stringResponse = sendAPIRequestPlain("problemPackage", "problem.package", problemId, packageId, type);
        try {
            JSONResponse json = gson.fromJson(stringResponse, JSONResponse.class);
            throw new PolygonSessionFailedRequestException(BASE_URL + "problem.package", json.getComment());
        } catch (JsonSyntaxException | NullPointerException ignored) {
        }

        HttpResponse response = getAPIResponse("problemPackage", "problem.package", problemId, packageId, type);

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

    /**
     * Returns problems of the contest.
     *
     * @param contestId Contest ID.
     * @return A map from problem letter to {@link Problem} object.
     */
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
        if (pin != null) {
            extendedParameters.add(new BasicNameValuePair("pin", pin));
        }
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

        JSONResponse jsonResponse;
        long timeout = 100;
        while (true) {
            if (timeout >= 60000) {
                throw new PolygonSessionException("Polygon API doesn't respond, please try later");
            }
            try {
                jsonResponse = gson.fromJson(json, JSONResponse.class);
                break;
            } catch (JsonSyntaxException e) {
                try {
                    Thread.currentThread().wait(timeout);
                } catch (InterruptedException ie) {
                    throw new PolygonSessionException("Session thread was interrupted", e);
                } catch (RuntimeException ie) {
                    System.err.println("WARNING: unexpected runtime error happened, retrying request...");
                    System.err.println(ie.getMessage());
                }
                timeout *= 2;
            }
        }

        if (jsonResponse.getStatus() == JSONResponseStatus.FAILED) {
            throw new PolygonSessionFailedRequestException(BASE_URL + methodName, parameters,
                    jsonResponse.getComment());
        }

        return jsonResponse.getResult();
    }

    private HttpResponse sendPostRequest(final String url, final List<NameValuePair> parameters)
            throws IOException {
        HttpPost request = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        for (NameValuePair p : parameters) {
            builder.addBinaryBody(p.getName(), p.getValue().getBytes(StandardCharsets.UTF_8));
        }
        HttpEntity entity = builder.build();
        request.setEntity(entity);
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
