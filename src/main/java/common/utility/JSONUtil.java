package common.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling JSON operations.
 */
public class JSONUtil {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Converts an object to a JSON string.
     *
     * @param obj Object to convert
     * @param clazz Class type of the object
     * @return JSON string representation
     * @throws Exception if serialization fails
     */
    public static <T> String marshal(T obj, Class<T> clazz) throws Exception {
        return gson.toJson(obj, clazz);
    }

    /**
     * Converts a JSON string to an object.
     *
     * @param json JSON string
     * @param clazz Class type to convert to
     * @return Object instance
     * @throws Exception if deserialization fails
     */
    public static <T> T unmarshal(String json, Class<T> clazz) throws Exception {
        return gson.fromJson(json, clazz);
    }

}