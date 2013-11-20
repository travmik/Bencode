package com.travmik.bittorrent.bencode;

import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.travmik.bittorrent.type.AbstractType;
import com.travmik.bittorrent.type.ByteStringType;
import com.travmik.bittorrent.type.DictionaryType;
import com.travmik.bittorrent.type.IntegerType;
import com.travmik.bittorrent.type.ListType;
import com.travmik.bittorrent.util.BenUtil;

public class BenInputStream extends FilterInputStream {

    private final BufferedInputStream input;

    public BenInputStream(final InputStream in) {
        this(in, false);
    }

    public BenInputStream(final InputStream in, final boolean subStream) {
        super(in);
        if(!subStream) {
            this.input = new BufferedInputStream(in);
        } else {
            this.input = (BufferedInputStream) in;
        }
    }

    public AbstractType readNextType() throws IOException {
        input.mark(0);
        final char symbol = (char) input.read();

        AbstractType type = null;
        switch(symbol) {
            case BenUtil.INTEGER_SYMBOL:
                type = IntegerType.parse(input);
                break;
            case BenUtil.LIST_SYMBOL:
                type = ListType.parse(input);
                break;
            case BenUtil.DICTIONARY_SYMBOL:
                type = DictionaryType.parse(input);
                break;
            case BenUtil.END_SYMBOL:
                return null;
        }
        if(Character.isDigit(symbol)) {
            input.reset();
            type = ByteStringType.parse(input);
        }
        return type;
    }
}
