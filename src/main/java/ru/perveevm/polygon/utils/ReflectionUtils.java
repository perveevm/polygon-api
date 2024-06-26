package ru.perveevm.polygon.utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import ru.perveevm.polygon.exceptions.api.PolygonSessionException;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents some utils working with Java reflection to perform requests easier.
 *
 * @author Mike Perveev (perveev_m@mail.ru)
 */
public class ReflectionUtils {
    /**
     * Encodes method parameters to {@link List} of {@link NameValuePair} objects.
     *
     * @param method {@link Method} object for some method from {@link ru.perveevm.polygon.api.PolygonSession} class.
     * @param values An array of method parameters.
     * @return Encoded parameters.
     * @throws PolygonSessionException if error occured while reading some file from file descriptor parameter.
     */
    public static List<NameValuePair> encodeMethodParameters(final Method method, final Object... values)
            throws PolygonSessionException {
        Parameter[] parameters = method.getParameters();
        List<NameValuePair> requestParameters = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            if (values[i] == null) {
                continue;
            }

            if (values[i] instanceof File) {
                File curFile = (File) values[i];
                String curData;
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(curFile),
                        StandardCharsets.UTF_8))) {
                    curData = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                } catch (IOException e) {
                    throw new PolygonSessionException("Cannot read file", e);
                }
                requestParameters.add(new BasicNameValuePair(parameters[i].getName(), curData));
            } else {
                requestParameters.add(new BasicNameValuePair(parameters[i].getName(), String.valueOf(values[i])));
            }
        }
        return requestParameters;
    }

    /**
     * Gets {@link Method} object by its name.
     *
     * @param clazz Class descriptor to find method in.
     * @param name  Method name.
     * @return {@link Method} object corresponding to found method.
     */
    public static Method getMethodByName(final Class<?> clazz, final String name) {
        Optional<Method> foundMethod = Arrays.stream(clazz.getMethods())
                .filter(p -> p.getName().equals(name)).findFirst();
        if (foundMethod.isEmpty()) {
            throw new IllegalArgumentException("There is no API method with name: " + name);
        } else {
            return foundMethod.get();
        }
    }
}
