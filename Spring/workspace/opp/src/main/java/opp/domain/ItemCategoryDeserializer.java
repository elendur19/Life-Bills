package opp.domain;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ItemCategoryDeserializer extends StdDeserializer<ItemCategory> {

    public ItemCategoryDeserializer() {
        this(null);
    }

    public ItemCategoryDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ItemCategory deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String itemCatName = node.get("name").asText();
        String subCatName = node.get("subCategory").asText();
        return new ItemCategory(itemCatName,new ItemCategory(subCatName));
    }
}
