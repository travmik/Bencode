package com.travmik.bittorrent.type;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class ByteStringType extends AbstractType {
    private final int length;
    private final byte[] value;

    private ByteStringType(final int length, final byte[] value) {
        this.length = length;
        this.value = value;
    }

    public static AbstractType parse(final BufferedInputStream input) throws IOException {
        int length = 0;

        for(char read = (char) input.read(); read != ':'; read = (char) input.read()) {
            length = (length * 10) + Character.getNumericValue(read);
        }
        final byte[] data = new byte[length];
        final int result = input.read(data);
        if(result < length) {
            System.out.println("FAILED");
        }
        return new ByteStringType(length, data);
    }

    @Override
    public void dump(final OutputStream output) throws IOException {
        output.write(Integer.toString(length).getBytes());
        output.write(':');
        output.write(value);
    }

    @Override
    public String getNativeValue() {
        return new String(value, Charset.forName("UTF-8"));
    }

    @Override
    public String toString() {
        return "[ByteStringType length=" + length + " value=" + getNativeValue() + "]";
    }
}
