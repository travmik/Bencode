package com.travmik.bittorrent.type;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.travmik.bittorrent.util.BenUtil;

public class IntegerType extends AbstractType {
    private final int value;

    private IntegerType(final int value) {
        this.value = value;
    }

    public static AbstractType parse(BufferedInputStream input) throws IOException {
        int value = 0;
        char read;
        while((read = (char) input.read()) != BenUtil.END_SYMBOL) {
            value = (value * 10) + Character.getNumericValue(read);
        }
        return new IntegerType(value);
    }

    @Override
    public void dump(final OutputStream output) throws IOException {
        output.write(BenUtil.INTEGER_SYMBOL);
        output.write(Integer.toString(value).getBytes());
        output.write(BenUtil.END_SYMBOL);
    }

    @Override
    public Integer getNativeValue() {
        return value;
    }

    @Override
    public String toString() {
        return "[IntegerType=" + value + "]";
    }
}
