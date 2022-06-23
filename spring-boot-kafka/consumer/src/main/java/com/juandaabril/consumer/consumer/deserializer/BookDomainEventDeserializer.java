package com.juandaabril.consumer.consumer.deserializer;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juandaabril.consumer.model.BookCreated;
import com.juandaabril.consumer.model.BookEvent;
import com.juandaabril.consumer.model.BookUpdated;

import java.io.IOException;

public class BookDomainEventDeserializer extends JsonDeserializer<BookEvent> {

    @Override
    public BookEvent deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JacksonException {
        final JsonNode node = parser.getCodec().readTree(parser);
        final String type = node.get("type").asText();

        final ObjectMapper mapper = (ObjectMapper)parser.getCodec();

        if ("BookCreated".equals(type)) {
            return  mapper.treeToValue(node, BookCreated.class);
        } else if("BookUpdated".equals(type))  {
            return  mapper.treeToValue(node, BookUpdated.class);
        }

        throw  new RuntimeException(String.format("BookEvent Type not found: type:{}%s", type));
    }
}
