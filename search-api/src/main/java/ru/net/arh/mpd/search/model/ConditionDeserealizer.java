package ru.net.arh.mpd.search.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ru.net.arh.mpd.search.model.PredicateCondition.PredicateType;

import java.io.IOException;
import java.util.*;

public class ConditionDeserealizer extends StdDeserializer<Condition> {

    public ConditionDeserealizer() {
        super(Condition.class);
    }

    @Override
    public Condition deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        ObjectMapper objectMapper = (ObjectMapper) p.getCodec();
        ObjectNode object = objectMapper.readTree(p);
        if (object.has("type")) {
            PredicateType type = PredicateType.valueOf(object.get("type").asText());
            JsonNode conditions = object.get("conditions");
            return new PredicateCondition(Arrays.asList(objectMapper.treeToValue(conditions, Condition[].class)), type);
        }
        Id3Tag id3Tag = Id3Tag.valueOf(object.get("id3Tag").asText());
        Id3Predicate operation = Id3Predicate.valueOf(object.get("operation").asText());
        String value = object.get("value").asText();
        return new SearchCondition(id3Tag, operation, value);
    }

}
