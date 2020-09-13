package app.utils;

import app.entity.GcpStorageFileLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Optional;

public class GcpStorageMessageParser {

    private static final String NAME = "name";
    private static final String BUCKET = "bucket";

    public static Optional<GcpStorageFileLocation> getFileLocationFromMessage(String payload) throws JsonProcessingException {
        final ObjectNode node = new ObjectMapper().readValue(payload, ObjectNode.class);
        Optional<GcpStorageFileLocation> result = Optional.empty();
        if (node.has(NAME) && node.has(BUCKET)) {
            result  = Optional.of(new GcpStorageFileLocation(node.get(NAME).textValue(), node.get(BUCKET).textValue()));
        }
        return result;
    }
}
