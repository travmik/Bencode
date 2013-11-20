package com.travmik.bittorrent.type;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class AbstractType {
    public static AbstractType parse(final BufferedInputStream input) throws IOException {
        throw new UnsupportedOperationException();
    }

    public void dump(final OutputStream output) throws IOException {
        throw new UnsupportedOperationException();
    }

    public abstract Object getNativeValue();
}
