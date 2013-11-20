package com.travmik.bittorrent.type;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.travmik.bittorrent.bencode.BenInputStream;

public class ListType extends AbstractType {
    private final List<AbstractType> elements;

    private  ListType(final List<AbstractType> elements) {
        this.elements = elements;
    }

    public static AbstractType parse(final BufferedInputStream input) throws IOException {
        final List<AbstractType> elements = new ArrayList<>();
        final BenInputStream benInput = new BenInputStream(input, true);
        while(true) {
            final AbstractType element = benInput.readNextType();
            if(element == null) {
                break;
            }
            elements.add(element);
        }
        return new ListType(elements);
    }

    @Override
    public void dump(final OutputStream output) throws IOException {
        output.write('l');
        for(final AbstractType element : elements) {
            element.dump(output);
        }
        output.write('e');
    }

    @Override
    public List<AbstractType> getNativeValue() {
        return elements;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("[ListType length=").append(elements.size()).append(" elements={");
        for(final AbstractType element : elements) {
            builder.append(element.toString()).append(", ");
        }
        return builder.append("}]").toString();
    }
}
