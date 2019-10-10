package io.gmartov;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.internal.JsonFormatter;
import com.jayway.jsonpath.internal.path.ArrayPathToken;
import com.jayway.jsonpath.internal.path.CompiledPath;
import com.jayway.jsonpath.internal.path.PathCompiler;
import com.jayway.jsonpath.internal.path.PathToken;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class JsonUtils {

    private static Configuration defaultConfig = Configuration.defaultConfiguration();
    private static Configuration config = defaultConfig;
    private static boolean nullIfNoPath = true;
    private static boolean isPrettify = true;

    public static void withConfig(Configuration configuration) {
        JsonUtils.config = configuration;
    }

    public static void returnNullIfNoPath(boolean b) {
        JsonUtils.nullIfNoPath = b;
    }

    public static void withPrettify(boolean b) {
        JsonUtils.isPrettify = b;
    }

    // FIXME make private
    public static DocumentContext parseJson(String json) {
        return JsonPath.using(config).parse(json);
    }

    public static boolean isPathExists(String json, String jsonPath) {
        try {
            parseJson(json).read(jsonPath);
            return true;
        } catch (PathNotFoundException e) {
            return false;
        }
    }

    public static <T> T getValue(String json, String jsonPath, Class<T> clazz) {
        try {
            return parseJson(json).read(jsonPath, clazz);
        } catch (PathNotFoundException e) {
            if (nullIfNoPath) {
                return null;
            }
            throw new PathNotFoundException(e.getMessage() + " in json: \n" + json);
        }
    }

    public static Object getValue(String json, String jsonPath) {
        try {
            return parseJson(json).read(jsonPath);
        } catch (PathNotFoundException e) {
            if (nullIfNoPath) {
                return null;
            }
            throw new PathNotFoundException(e.getMessage() + " in json: \n" + json);
        }
    }

    public enum Action {
        DELETE,
        DO_NOT_CHANGE
    }

    public static String prettify(String json) {
        return isPrettify ? JsonFormatter.prettyPrint(json) : json;
    }

    public static String setJsonAttribute(String json, String jsonPath, Object value) {
        DocumentContext dc = parseJson(json);
        if (value == Action.DELETE) {
//            LOGGER.debug("Going to delete: " + jsonPath);
            dc.delete(jsonPath);
        } else if (value == Action.DO_NOT_CHANGE) {
//            LOGGER.debug("Going to leave value as is: " + jsonPath);
        } else if (isPathExists(json, jsonPath)) {
//            LOGGER.debug("Going to update: " + jsonPath + " value: " + (value == null ? "null" : value.toString()));
            dc.set(jsonPath, value);
        } else {
            final List<String> crumbs = pathList(jsonPath);
            int i = 1;
            while (i < crumbs.size()) {
                // set -> path, value ? should path exists
                // put -> path[], key, value
                // add -> array -> array element


                if (!isPathExists(json, crumbs.get(i))) {
                    dc.put(crumbs.get(i-1), crumbs.get(i), null);
                }
                ++i;
            }
            throw new NotImplementedException();
        }

        return prettify(dc.jsonString());
    }

    private static List<String> pathList(String jsonPath) {
        final CompiledPath p = (CompiledPath) PathCompiler.compile(jsonPath);
        List<String> res = new ArrayList<>();
        try {
            Field r = CompiledPath.class.getDeclaredField("root");
            r.setAccessible(true);
            Field n = PathToken.class.getDeclaredField("next");
            n.setAccessible(true);

            PathToken t = (PathToken) r.get(p);
            final int tokenCount = t.getTokenCount();
            final String base = t.toString();
            for (int i = 0; i < tokenCount; i++) {
                t = (PathToken) n.get(t);
                final String tail = null == t ? "" : t.toString();
                if (!(t instanceof ArrayPathToken)) {
                    res.add(base.replace(tail, ""));
                }
            }
            return res;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
