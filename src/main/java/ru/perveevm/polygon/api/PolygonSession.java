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
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import ru.perveevm.polygon.api.entities.*;
import ru.perveevm.polygon.api.entities.enums.*;
import ru.perveevm.polygon.api.json.JSONResponse;
import ru.perveevm.polygon.api.json.JSONResponseStatus;
import ru.perveevm.polygon.exceptions.api.*;
import ru.perveevm.polygon.utils.HttpUtils;
import ru.perveevm.polygon.utils.ReflectionUtils;

import javax.imageio.stream.IIOByteBuffer;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Basic class for performing Polygon API calls.
 *
 * @author Mike Perveev (perveev_m@mail.ru)
 */
public class PolygonSession implements Closeable {
    private final static String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private final Random random = new Random();
    private final Gson gson = new Gson();


    private final String key;
    private final String secret;

    private String baseUrl = "https://polygon.codeforces.com/api/";
    private CloseableHttpClient client = HttpClients.createDefault();

    private long startWaitMs = 100L;
    private long maxTotalWaitMs = 60000L;
    private double waitCoefficient = 2.0;
    private int maxRetries = 5;

    private String pin = null;

    PolygonSession(final String key, final String secret) {
        this.key = key;
        this.secret = secret;
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    void setStartWaitMs(final long startWaitMs) {
        this.startWaitMs = startWaitMs;
    }

    void setMaxTotalWaitMs(final long maxTotalWaitMs) {
        this.maxTotalWaitMs = maxTotalWaitMs;
    }

    void setWaitCoefficient(final double waitCoefficient) {
        this.waitCoefficient = waitCoefficient;
    }

    void setMaxRetries(final int maxRetries) {
        this.maxRetries = maxRetries;
    }

    void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    void setClient(final CloseableHttpClient client) {
        this.client = client;
    }

    /**
     * Sets <code>pin</code> that will be used in all following requests. You can set it to <code>null</code> if no pin
     * is required.
     *
     * @param pin Polygon pin code for problem or contest access.
     */
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    public Problem[] problemsList(final Boolean showDeleted, final Integer id, final String name, final String owner)
            throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemsList", "problems.list", showDeleted, id, name, owner),
                Problem[].class);
    }

    /**
     * Create a new empty problem. Returns a created Problem.
     *
     * @param name Name of problem being created.
     * @return A created {@link Problem}.
     */
    @SuppressWarnings("unused")
    public Problem problemCreate(@NonNull final String name) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemCreate", "problem.create", name), Problem.class);
    }

    /**
     * Returns problem general info.
     *
     * @param problemId Problem ID.
     * @return {@link ProblemInfo} object.
     */
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    public void problemUpdateInfo(@NonNull final Integer problemId, final String inputFile, final String outputFile,
                                  final Boolean interactive, final Integer timeLimit, final Integer memoryLimit)
            throws PolygonSessionException {
        sendAPIRequest("problemUpdateInfo", "problem.updateInfo", problemId, inputFile, outputFile,
                interactive, timeLimit, memoryLimit);
    }

    /**
     * Updates working copy.
     *
     * @param problemId Problem ID.
     */
    @SuppressWarnings("unused")
    public void problemUpdateWorkingCopy(@NonNull final Integer problemId) throws PolygonSessionException {
        sendAPIRequest("problemUpdateWorkingCopy", "problem.updateWorkingCopy", problemId);
    }

    /**
     * Discards working copy.
     *
     * @param problemId Problem ID.
     */
    @SuppressWarnings("unused")
    public void problemDiscardWorkingCopy(@NonNull final Integer problemId) throws PolygonSessionException {
        sendAPIRequest("problemDiscardWorkingCopy", "problem.discardWorkingCopy", problemId);
    }

    /**
     * Commits problem changes. All parameters are optional.
     *
     * @param problemId    Problem ID.
     * @param minorChanges If <code>true</code>, no email notification will be sent.
     * @param message      Problem’s commit message.
     */
    @SuppressWarnings("unused")
    public void problemCommitChanges(@NonNull final Integer problemId, final Boolean minorChanges, final String message)
            throws PolygonSessionException {
        sendAPIRequest("problemCommitChanges", "problem.commitChanges", problemId, minorChanges,
                message);
    }

    /**
     * Returns a {@link Map} from language to a {@link Statement} object for that language.
     *
     * @param problemId Problem ID.
     * @return A described {@link Map}.
     */
    @SuppressWarnings("unused")
    public Map<String, Statement> problemStatements(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemStatements", "problem.statements", problemId),
                new TypeToken<Map<String, Statement>>() {
                }.getType());
    }

    /**
     * Update or create a problem’s statement. All parameters except for <code>lang</code> and <code>problemId</code>
     * can be <code>null</code>.
     *
     * @param problemId   Problem ID.
     * @param lang        Problem language.
     * @param encoding    Statement encoding.
     * @param name        Problem name.
     * @param legend      Problem legend.
     * @param input       Problem input format.
     * @param output      Problem output format.
     * @param scoring     Problem scoring description.
     * @param interaction Problem interaction protocol (only for interactive problems)
     * @param notes       Problem notes.
     * @param tutorial    Problem tutorial.
     */
    @SuppressWarnings("unused")
    public void problemSaveStatement(@NonNull final Integer problemId, @NonNull final String lang,
                                     final String encoding, final String name, final String legend, final String input,
                                     final String output, final String scoring, final String interaction,
                                     final String notes, final String tutorial) throws PolygonSessionException {
        sendAPIRequest("problemSaveStatement", "problem.saveStatement", problemId, lang, encoding,
                name, legend, input, output, scoring, interaction, notes, tutorial);
    }

    /**
     * Returns a list of statement resources for the problem.
     *
     * @param problemId Problem ID.
     * @return An array of {@link ProblemFile} objects representing resources.
     */
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    public String problemChecker(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemChecker", "problem.checker", problemId), String.class);
    }

    /**
     * Returns the name of currently set validator.
     *
     * @param problemId Problem ID.
     * @return {@link String} object, contains validator name.
     */
    @SuppressWarnings("unused")
    public String problemValidator(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemValidator", "problem.validator", problemId), String.class);
    }

    /**
     * Returns the name of currently set interactor.
     *
     * @param problemId Problem ID.
     * @return {@link String} object, contains interactor name.
     */
    @SuppressWarnings("unused")
    public String problemInteractor(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemInteractor", "problem.interactor", problemId), String.class);
    }

    /**
     * Returns a list of validator tests for the problem.
     *
     * @param problemId Problem ID.
     * @return An array of {@link ValidatorTest} objects.
     */
    @SuppressWarnings("unused")
    public ValidatorTest[] problemValidatorTests(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemValidatorTests", "problem.validatorTests", problemId), ValidatorTest[].class);
    }

    /**
     * Add or edit validator test. All parameters except for <code>testIndex</code>, <code>testInput</code>
     * and <code>testVerdict</code> are optional.
     *
     * @param problemId     Problem ID.
     * @param checkExisting If <code>true</code>, only adding validator’s test is allowed.
     * @param testVerdict   Validator test verdict.
     * @param testIndex     Index of a validator test.
     * @param testInput     Input of a validator test.
     * @param testGroup     Test group (groups should be enabled for the testset).
     * @param testset       Testset name.
     */
    @SuppressWarnings("unused")
    public void problemSaveValidatorTest(@NonNull final Integer problemId, final Boolean checkExisting,
                                         @NonNull final ValidatorTestVerdict testVerdict,
                                         @NonNull final Integer testIndex, @NonNull final String testInput,
                                         final String testGroup, final String testset) throws PolygonSessionException {
        sendAPIRequest("problemSaveValidatorTest", "problem.saveValidatorTest", problemId,
                checkExisting, testVerdict, testIndex, testInput, testGroup, testset);
    }

    /**
     * Returns a list of checker tests for the problem.
     *
     * @param problemId Problem ID.
     * @return An array of {@link CheckerTest} objects.
     */
    @SuppressWarnings("unused")
    public CheckerTest[] problemCheckerTests(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemCheckerTests",
                "problem.checkerTests", problemId), CheckerTest[].class);
    }

    /**
     * Adds or edits checker test. All parameters except for <code>testIndex</code>, <code>testInput</code>,
     * <code>testAnswer</code>, <code>testOutput</code> and <code>testVerdict</code> are optional.
     *
     * @param problemId     Problem ID.
     * @param checkExisting If <code>true</code>, only adding checker test is allowed.
     * @param testVerdict   Checker’s test verdict.
     * @param testIndex     Index of a checker test.
     * @param testInput     Input of a checker test.
     * @param testOutput    Output of a checker test.
     * @param testAnswer    Answer of a checker test.
     */
    @SuppressWarnings("unused")
    public void problemSaveCheckerTest(@NonNull final Integer problemId, final Boolean checkExisting,
                                       @NonNull final CheckerTestVerdict testVerdict,
                                       @NonNull final Integer testIndex, @NonNull final String testInput,
                                       @NonNull final String testOutput, @NonNull final String testAnswer)
            throws PolygonSessionException {
        sendAPIRequest("problemSaveCheckerTest", "problem.saveCheckerTest", problemId, checkExisting,
                testVerdict, testIndex, testInput, testOutput, testAnswer);
    }

    /**
     * Returns the list of resource, source and aux files.
     *
     * @param problemId Problem ID.
     * @return {@link ProblemFiles} object.
     */
    @SuppressWarnings("unused")
    public ProblemFiles problemFiles(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemFiles", "problem.files", problemId), ProblemFiles.class);
    }

    /**
     * Returns the list of problem solutions.
     *
     * @param problemId Problem ID.
     * @return An array of {@link Solution} objects.
     */
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    public String problemViewFile(@NonNull final Integer problemId, @NonNull final String type,
                                  @NonNull final String name) throws PolygonSessionException {
        String result = sendAPIRequestPlain("problemViewFile", "problem.viewFile", problemId, type, name);
        try {
            JSONResponse response = gson.fromJson(result, JSONResponse.class);
            throw new PolygonSessionFailedRequestException(baseUrl + "problem.viewFile", response.getComment());
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
    @SuppressWarnings("unused")
    public String problemViewSolution(@NonNull final Integer problemId, @NonNull final String name)
            throws PolygonSessionException {
        String result = sendAPIRequestPlain("problemViewSolution", "problem.viewSolution", problemId, name);
        try {
            JSONResponse response = gson.fromJson(result, JSONResponse.class);
            throw new PolygonSessionFailedRequestException(baseUrl + "problem.viewSolution", response.getComment());
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
    @SuppressWarnings("unused")
    public String problemScript(@NonNull final Integer problemId, @NonNull final String testset)
            throws PolygonSessionException {
        String result = sendAPIRequestPlain("problemScript", "problem.script", problemId, testset);
        try {
            JSONResponse response = gson.fromJson(result, JSONResponse.class);
            throw new PolygonSessionFailedRequestException(baseUrl + "problem.script", response.getComment());
        } catch (JsonSyntaxException | NullPointerException ignored) {
            return result;
        }
    }

    /**
     * Returns tests for the given testset.
     *
     * @param problemId Problem ID.
     * @param testset   Testset name.
     * @param noInputs  If <code>true</code>, returns tests without input.
     * @return An array of {@link ProblemTest} objects.
     */
    @SuppressWarnings("unused")
    public ProblemTest[] problemTests(@NonNull final Integer problemId, @NonNull final String testset,
                                      final Boolean noInputs)
            throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemTests", "problem.tests", problemId, testset,
                noInputs), ProblemTest[].class);
    }

    /**
     * Returns generated test input.
     *
     * @param problemId Problem ID.
     * @param testset   Testset name.
     * @param testIndex Test index.
     * @return Test input content in a {@link String} object.
     */
    @SuppressWarnings("unused")
    public String problemTestInput(@NonNull final Integer problemId, @NonNull final String testset,
                                   @NonNull final Integer testIndex) throws PolygonSessionException {
        String result = sendAPIRequestPlain("problemTestInput", "problem.testInput", problemId, testset, testIndex);
        try {
            JSONResponse response = gson.fromJson(result, JSONResponse.class);
            throw new PolygonSessionFailedRequestException(baseUrl + "problem.testInput", response.getComment());
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
    @SuppressWarnings("unused")
    public String problemTestAnswer(@NonNull final Integer problemId, @NonNull final String testset,
                                    @NonNull final Integer testIndex) throws PolygonSessionException {
        String result = sendAPIRequestPlain("problemTestAnswer", "problem.testAnswer", problemId, testset, testIndex);
        try {
            JSONResponse response = gson.fromJson(result, JSONResponse.class);
            throw new PolygonSessionFailedRequestException(baseUrl + "problem.testAnswer", response.getComment());
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    public String[] problemViewTags(@NonNull final Integer problemId) throws PolygonSessionException {
        return gson.fromJson(sendAPIRequest("problemViewTags", "problem.viewTags", problemId), String[].class);
    }

    /**
     * Saves tags for the problem. Existed tags will be replaced by new tags.
     *
     * @param problemId Problem ID.
     * @param tags      Array of new tags.
     */
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    public void problemPackage(@NonNull final Integer problemId, @NonNull final Integer packageId, final String type,
                               @NonNull final File downloadPath)
            throws PolygonSessionException {
        List<NameValuePair> parameters = ReflectionUtils.encodeMethodParameters(
                ReflectionUtils.getMethodByName(this.getClass(), "problemPackage"), problemId, packageId, type);
        HttpUtils.downloadFile(baseUrl + "problem.package",
                getAPIResponse("problem.package", parameters), downloadPath);
    }

    /**
     * Starts to build a new package.
     *
     * @param problemId Problem ID.
     * @param full      Defines whether to build full package, contains "standard", "linux" and "windows" packages,
     *                  or only "standard". Standard packages don't contain generated tests, but contain windows
     *                  executables and scripts for windows and linux via wine. Linux packages contain generated tests,
     *                  but don't contain compiled binaries. Windows packages contain generated tests and compiled
     *                  binaries for Windows.
     * @param verify    If that parameter is <code>true</code> all solutions will be invoked on all tests to be sure
     *                  that tags are valid. Stress tests will run the checker to verify its credibility.
     */
    @SuppressWarnings("unused")
    public void problemBuildPackage(@NonNull final Integer problemId, @NonNull final Boolean full,
                                    @NonNull final Boolean verify) throws PolygonSessionException {
        sendAPIRequest("problemBuildPackage", "problem.buildPackage", problemId, full, verify);
    }

    /**
     * Returns problems of the contest.
     *
     * @param contestId Contest ID.
     * @return A map from problem letter to {@link Problem} object.
     */
    @SuppressWarnings("unused")
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
            response = HttpUtils.sendPostRequest(client, baseUrl + methodName, extendedParameters);
        } catch (IOException e) {
            throw new PolygonSessionHTTPErrorException(baseUrl + methodName, parameters, e);
        }

        return response;
    }

    private String sendAPIRequestPlain(final String methodName, final List<NameValuePair> parameters)
            throws PolygonSessionException {
        HttpResponse response = getAPIResponse(methodName, parameters);

        String responseText;
        try {
            responseText = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (IOException | ParseException e) {
            throw new PolygonSessionBadResponseException(baseUrl + methodName, parameters,
                    response.getStatusLine().getStatusCode(), e);
        }

        return responseText;
    }

    private JsonElement sendAPIRequest(final String methodName, final List<NameValuePair> parameters)
            throws PolygonSessionException {
        JSONResponse jsonResponse;
        long delay = startWaitMs;
        long totalDelay = 0;
        int retries = 0;
        do {
            try {
                String json = sendAPIRequestPlain(methodName, parameters);
                jsonResponse = gson.fromJson(json, JSONResponse.class);
                break;
            } catch (JsonSyntaxException e) {
                long waitFor = Math.min(delay, maxTotalWaitMs - totalDelay);
                ++retries;
                delay = (long) (delay * waitCoefficient);
                if (waitFor <= 0 || retries > maxRetries) {
                    throw new PolygonSessionAPIUnavailableException();
                }

                try {
                    Thread.sleep(waitFor);  // TODO: may be remove busy wait here
                } catch (InterruptedException es) {
                    throw new PolygonSessionException("Session thread was interrupted", e);
                }
            }
        } while (true);

        if (jsonResponse.getStatus() == JSONResponseStatus.FAILED) {
            throw new PolygonSessionFailedRequestException(baseUrl + methodName, parameters,
                    jsonResponse.getComment());
        }
        return jsonResponse.getResult();
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
