package opp.domain;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class UserSerializer  extends StdSerializer<User> {
    public UserSerializer() {
        this(null);
    }

    public UserSerializer(Class<User> t) {
        super(t);
    }

    @Override
    public void serialize(User value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException{
        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("username", value.getUsername());
        jgen.writeStringField("password", value.getPassword());
        jgen.writeStringField("email", value.getEmail());
        jgen.writeStringField("oib", value.getOib());
        jgen.writeStringField("dateOfBirth", value.getDateOfBirth());
        jgen.writeStringField("address", value.getAddress());
        jgen.writeStringField("contactNumber", value.getContactNumber());
        jgen.writeStringField("name", value.getName());
        jgen.writeStringField("lastname", value.getLastname());

        jgen.writeArrayFieldStart("usersBills");
        for (Bill bill : value.getUsersBills()) {
            jgen.writeStartObject();
            jgen.writeNumberField("billsId", bill.getId());
            jgen.writeEndObject();
        }
        jgen.writeEndArray();

        jgen.writeArrayFieldStart("otherUsersBills");
        for (Bill bill : value.getOtherUsersBills()) {
            jgen.writeStartObject();
            jgen.writeNumberField("othersBillsId", bill.getId());
            jgen.writeEndObject();
        }
        jgen.writeEndArray();

        jgen.writeEndObject();
    }
}
