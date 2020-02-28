package com.naigoapps.restaurant.services.fiscal.hydra.commands;

import com.naigoapps.restaurant.services.fiscal.hydra.Codes;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.Field;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.IntegerField;
import java.nio.ByteBuffer;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public abstract class AbstractRequest implements Request {

    protected static byte[] wrap(List<Field> fields) {
        int seps = fields.size();
        int fieldsSize = fields.stream().mapToInt(Field::size).sum();
        int totalSize = 1 + seps + fieldsSize + 2 + 1;
        ByteBuffer bb = ByteBuffer.allocate(totalSize);
        int index = 1;
        bb.position(index);
        for (Field f : fields) {
            bb.put(f.value());
            index += f.size();
            bb.position(index);
            bb.put(Codes.SEP);
            index++;
            bb.position(index);
        }
        int checksum = 0;
        for (int i = 0; i < bb.array().length; i++) {
            checksum += ((int) bb.get(i)) & 0xFF;
            checksum = checksum & 0xFF;
        }
        checksum %= 100;
        IntegerField checksumField = new IntegerField(checksum, 2);

        bb.position(0);
        bb.put(Codes.PRE);
        bb.position(1 + seps + fieldsSize);
        bb.put(checksumField.value());
        bb.put(Codes.POST);
        return bb.array();
    }

    protected String trim(String item, int size) {
        if(item.length() > size) {
            return item.substring(0, size - 1) + ".";
        }
        return item;
    }

    protected String integer(int val) {
        return String.valueOf(val);
    }

    protected String amount(double val) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
        return format.format(val).substring(1);
    }

    @Override
    public int getExpectedResponses() {
        return 2;
    }
}
