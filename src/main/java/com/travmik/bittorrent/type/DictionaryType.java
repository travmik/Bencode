package com.travmik.bittorrent.type;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.travmik.bittorrent.bencode.BenInputStream;

public class DictionaryType extends AbstractType {
    private final List<DictionaryElement> elements;

    private DictionaryType(final List<DictionaryElement> elements) {
        this.elements = elements;
    }

    public static AbstractType parse(final BufferedInputStream input) throws IOException {
        final List<DictionaryElement> elements = new ArrayList<>();
        final BenInputStream benInput = new BenInputStream(input, true);
        while(true) {
            final ByteStringType key = (ByteStringType) benInput.readNextType();
            if(key == null) {
                break;
            }
            final AbstractType value = benInput.readNextType();
            elements.add(new DictionaryElement(key, value));
        }
        return new DictionaryType(elements);
    }

    @Override
    public void dump(final OutputStream output) throws IOException {
        output.write('d');
        for(final DictionaryElement entry : elements) {
            entry.getKey().dump(output);
            entry.getValue().dump(output);
        }
        output.write('e');
    }

    @Override
    public List<DictionaryElement> getNativeValue() {
        return elements;
    }

    public void reverse() {
        //TODO: to be implemented
    }

    public AbstractType lookup(final String key) {
        for(final DictionaryElement element : elements) {
            if(element.getKey().getNativeValue().equals(key)) {
                return element.getValue();
            }
        }
        return new EmptyType();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("[DictionaryType length=").append(elements.size()).append(" elements={");
        for(final DictionaryElement entry : elements) {
            builder.append(entry.toString());
            builder.append(", ");

        }
        return builder.append("}]").toString();
    }

    public static class DictionaryElement {
        private final ByteStringType key;
        private final AbstractType value;

        public DictionaryElement(final ByteStringType key, final AbstractType value) {
            this.key = key;
            this.value = value;
        }

        public ByteStringType getKey() {
            return key;
        }

        public AbstractType getValue() {
            return value;
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("[DictionaryElement key=").append(key.toString()).append(" value=").
                    append(value.toString()).append("]").append("}]");
            return builder.toString();
        }
    }
}