package com.naigoapps.restaurant.services.fiscal.hydra.commands;

import com.naigoapps.restaurant.services.fiscal.hydra.fields.ByteField;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.Field;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.StringField;
import java.util.Arrays;
import java.util.List;

public class OpenReceiptRequest extends AbstractRequest {

    private final byte code = '>';

    private List<Field> getContent(){
        return Arrays.asList(
            new ByteField(code),
            new StringField(""),
            new StringField(""),
            new StringField("")
        );
    }

    @Override
    public byte[] getBytes() {
        return wrap(getContent());
    }
}
